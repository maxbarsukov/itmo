#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
    @Author: github.com/hackmemory
"""

from functools import reduce
import json
import random
import xml.etree.ElementTree as ET
import re
import binascii
import codecs
import argparse

def decode_tags(data):
    repls = [',','\\','\n',' ', '\r\n', '\r']
    pattern = r'<(\d+)>(.*?)<\/\1>'    
    tags = re.findall(pattern, data, re.DOTALL)
    decoded_tags = []
    for tag_id, hex_data in tags:
        hex_data = reduce(lambda a, v: a.replace(v,''), repls, hex_data)
        decoded_data = binascii.unhexlify(hex_data)
        decoded_data = codecs.decode(decoded_data, 'cp1251')
        decoded_tags.append(f'<{tag_id}>\n{decoded_data}</{tag_id}>')


    gr_pattern = r'<gr-id>(.*?)</gr-id>'    
    gr_data = re.findall(gr_pattern, data, re.DOTALL)
    hex_data = reduce(lambda a, v: a.replace(v,''), repls, gr_data[0])
    gr_data = binascii.unhexlify(hex_data)
    gr_data = codecs.decode(gr_data, 'cp1251')

    return decoded_tags, gr_data

def process_questions(xml_data):
    repls = ['\n','\r\n', '\r']
    question_blocks = re.findall(r'<\d+>.*?</\d+>', xml_data, re.DOTALL)
    questions_dict = {}

    for question_block in question_blocks:
        question_id = re.search(r'<(\d+)>', question_block).group(1)

        question = re.search(r'<question>(.*?)</question>', question_block, re.DOTALL).group(1).strip()
        question = reduce(lambda a, v: a.replace(v,''), repls, question)
        question_data = {
            'question': question,
            'type': int(re.search(r'type=(\d+)', question_block).group(1)),
            'right': int(re.search(r'right=(\d+)', question_block).group(1)),
        }

        answers = re.findall(r'<a_\d+>(.*?)</a_\d+>', question_block, re.DOTALL)
        question_data['answers'] = [answer.strip() for answer in answers]

        questions_dict[question_id] = question_data

    return questions_dict


def process_group_list(gr_data):
    gr_out = {}

    gr_list = re.search(r'<GR-List>(.*?)</GR-List>', gr_data, re.DOTALL)
    if gr_list:
        gr_list_content = gr_list.group(1).strip()
        gr_list_array = gr_list_content.split('\n')
        gr_out['GR-List'] = [s.replace('\r', '') for s in gr_list_array]

    gr_tags = re.findall(r'<GR-(\d+)>(.*?)</GR-(\d+)>', gr_data, re.DOTALL)
    for gr_tag in gr_tags:
        gr_number = gr_tag[0]
        gr_content = gr_tag[1].strip()
        tv_d = re.search(r'<tv_d>(.*?)</tv_d>', gr_content, re.DOTALL)

        if tv_d:
            tv_d_content = tv_d.group(1).strip()
            tv_d_array = tv_d_content.split('\n')
            gr_out[gr_number] = [s.replace('\r', '') for s in tv_d_array]
    
    return gr_out


def main():
    parser = argparse.ArgumentParser(description='Decode FDB file')
    parser.add_argument('fdb_file', type=str, help='Input FDB file')
    parser.add_argument('html_file', type=str, help='Output html file')
    args = parser.parse_args()

    with open(args.fdb_file, 'r', encoding='cp1251') as f:
        fdb_data = f.read()

    decoded_tags, gr_data = decode_tags(fdb_data)
    xml_data = "\n".join(decoded_tags)

    questions = process_questions(xml_data)
    gr_data = process_group_list(gr_data)

    grouped_questions = {}

    group_data = {
        'group_name': 'All Questions',
        'questions': list(questions.values())
    }

    grouped_questions = {'0': group_data}



    #print(json.loads(json.dumps(grouped_questions)))
    #html_content = generate_html(grouped_questions)
    html_content = generate_test_html(grouped_questions)

    with open(args.html_file, "w", encoding="utf-8") as html_file:
        html_file.write(html_content)


js_text = '''
<script>
document.querySelector("button").addEventListener("click", (e) => {
    if (e.target.innerText === "Enable test mode") {
        const style = document.createElement("style");
        style.textContent = "answers{visibility:hidden;}question:hover+answers{visibility:visible;}";
        document.head.appendChild(style);
        e.target.innerText = "Disable test mode";
    } else {
        document.head.removeChild(document.head.querySelector("style"));
        e.target.innerText = "Enable test mode";
    }
});
</script>

<script>
var coll = document.getElementsByClassName("collapsible");
var i;

for (i = 0; i < coll.length; i++) {
  coll[i].addEventListener("click", function() {
    this.classList.toggle("active");
    var content = this.nextElementSibling;
    if (content.style.display === "block") {
      content.style.display = "none";
    } else {
      content.style.display = "block";
    }
  });
}
</script>
'''

css_text = '''
<style>
.collapsible {
  background-color: #eee;
  color: #444;
  cursor: pointer;
  padding: 18px;
  width: 100%;
  border: none;
  text-align: left;
  outline: none;
  font-size: 15px;
}
.active, .collapsible:hover {
  background-color: #ccc;
}
.content {
  padding: 0 18px;
  display: none;
  overflow: hidden;
  background-color: #f1f1f1;
}
</style>
'''


def generate_html(grouped_questions):
    html_page = '<html><head><title>Test</title></head><body>'
    html_page += css_text
    html_page += '<button>Enable test mode</button><br><br>'

    for group_id, group_data in grouped_questions.items():
        #html_page += f'<h1>{group_data["group_name"]}</h1>'
        html_page += f"<button type='button' class='collapsible'><h2>{group_data['group_name']}</h2></button>"
        html_page += "<div class='content'>"
        html_page += '<ol>'
        for question in group_data['questions']:
            html_page += '<hr>'
            html_page += '<li>'
            html_page += f'<question type={question["type"]}><p>{question["question"]}</p></question>'
            html_page += '<answers>'
            html_page += f'<ul>'
            
            if question["type"] == 1:
                answers = question["answers"]
                for i, answer in enumerate(answers):
                    if i < question["right"]:
                        html_page += f'<li><strong>{answer}</strong></li>'
                    else:
                        html_page += f'<li>{answer}</li>'
            
            elif question["type"] == 2:
                answers = question["answers"]
                for i, answer in enumerate(answers):
                    if i < question["right"]:
                        html_page += f'<li><strong>{answer}</strong></li>'
                    else:
                        html_page += f'<li>{answer}</li>'
            
            elif question["type"] == 3:
                answers = question["answers"]
                for i, answer in enumerate(answers):
                    html_page += f'<li><strong>{i + 1}. {answer}</strong></li>'
            
            elif question["type"] == 6:
                answers = question["answers"]
                for answer in answers:
                    html_page += f'<li><strong>{answer}</strong></li>'
            
            elif question["type"] == 7:
                answers = question["answers"]
                for answer in answers:
                    html_page += f'<li><strong>{answer}</strong></li>'

            html_page += '</ul>'
            html_page += '</answers>'
            html_page += '</li>'

        html_page += '</ol>'
        html_page += '</div>'
    
    html_page += js_text
    html_page += '</body></html>'
    return html_page

def generate_test_html(grouped_questions):
    # Генерация HTML-страницы
    html_code = '''
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Interactive Test</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            body {
                background-color: #121212; /* Цвет фона страницы */
                color: #ffffff; /* Цвет текста */
                background: dark;
                color-scheme: dark;
            }

            .container {
                background-color: #181818; /* Цвет фона контейнеров */
                padding: 20px;
                border-radius: 10px;
            }

            .jumbotron {
                background-color: #282828; /* Цвет фона блока с вопросом */
                padding: 20px;
                border-radius: 10px;
            }

            .form-group {
                margin-bottom: 20px;
            }

            .btn-primary,
            .btn-secondary {
                margin-right: 10px;
            }

            .form-control {
                background-color: #343a40; 
                color: #ffffff; 
                border-color: rgb(60, 65, 68);
            }


            #answers-container {
                color: #ffffff;
            }

            .custom-checkbox {
            position: absolute;
            z-index: -1;
            opacity: 0;
            }

            /* для элемента label, связанного с .custom-checkbox */
            .custom-checkbox+label {
            display: inline-flex;
            align-items: center;
            user-select: none;
            }

            /* создание в label псевдоэлемента before со следующими стилями */
            .custom-checkbox+label::before {
            content: '';
            display: inline-block;
            width: 1em;
            height: 1em;
            flex-shrink: 0;
            flex-grow: 0;
            border: 1px solid #adb5bd;
            border-radius: 0.25em;
            margin-right: 0.5em;
            background-repeat: no-repeat;
            background-position: center center;
            background-size: 50% 50%;
            }

            /* стили при наведении курсора на checkbox */
            .custom-checkbox:not(:disabled):not(:checked)+label:hover::before {
            border-color: #b3d7ff;
            }

            /* стили для активного чекбокса (при нажатии на него) */
            .custom-checkbox:not(:disabled):active+label::before {
            background-color: #b3d7ff;
            border-color: #b3d7ff;
            }

            /* стили для чекбокса, находящегося в фокусе */
            .custom-checkbox:focus+label::before {
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
            }

            /* стили для чекбокса, находящегося в фокусе и не находящегося в состоянии checked */
            .custom-checkbox:focus:not(:checked)+label::before {
            border-color: #80bdff;
            }

            /* стили для чекбокса, находящегося в состоянии checked */
            .custom-checkbox:checked+label::before {
            border-color: #0b76ef;
            background-color: #0b76ef;
            background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 8 8'%3e%3cpath fill='%23fff' d='M6.564.75l-3.59 3.612-1.538-1.55L0 4.26 2.974 7.25 8 2.193z'/%3e%3c/svg%3e");
            }

            /* стили для чекбокса, находящегося в состоянии disabled */
            .custom-checkbox:disabled+label::before {
            background-color: #e9ecef;
            }


            /* для элемента input c type="radio" */
            .custom-radio>input {
            position: absolute;
            z-index: -1;
            opacity: 0;
            }

            /* для элемента label связанного с .custom-radio */
            .custom-radio>span {
            display: inline-flex;
            align-items: center;
            user-select: none;
            }

            /* создание в label псевдоэлемента  before со следующими стилями */
            .custom-radio>span::before {
            content: '';
            display: inline-block;
            width: 1em;
            height: 1em;
            flex-shrink: 0;
            flex-grow: 0;
            border: 1px solid #adb5bd;
            border-radius: 50%;
            margin-right: 0.5em;
            background-repeat: no-repeat;
            background-position: center center;
            background-size: 50% 50%;
            }

            /* стили при наведении курсора на радио */
            .custom-radio>input:not(:disabled):not(:checked)+span:hover::before {
            border-color: #b3d7ff;
            }

            /* стили для активной радиокнопки (при нажатии на неё) */
            .custom-radio>input:not(:disabled):active+span::before {
            background-color: #b3d7ff;
            border-color: #b3d7ff;
            }

            /* стили для радиокнопки, находящейся в фокусе */
            .custom-radio>input:focus+span::before {
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
            }

            /* стили для радиокнопки, находящейся в фокусе и не находящейся в состоянии checked */
            .custom-radio>input:focus:not(:checked)+span::before {
            border-color: #80bdff;
            }

            /* стили для радиокнопки, находящейся в состоянии checked */
            .custom-radio>input:checked+span::before {
            border-color: #0b76ef;
            background-color: #0b76ef;
            background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='-4 -4 8 8'%3e%3ccircle r='3' fill='%23fff'/%3e%3c/svg%3e");
            }

            /* стили для радиокнопки, находящейся в состоянии disabled */
            .custom-radio>input:disabled+span::before {
            background-color: #e9ecef;
            }

            
        </style>
        <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </head>
    <body>
    '''

    html_code = html_code + """
    <script>
        var questionsData = %s;
        var currentGroup = "0";
        var currentQuestionIndex = 0;
        var originalAnswersOrder; // Добавлено для хранения оригинального порядка ответов

        function removeNewLines(str) {
            return str.replace(/(\\r\\n|\\n|\\r)/gm, "");
        }

        function showQuestion() {
            var questionData = questionsData[currentGroup].questions[currentQuestionIndex];
            var questionContainer = $("#question-container");
            questionContainer.html(`<p>${currentQuestionIndex + 1}. ${questionData.question}</p>`);
            
            var answersContainer = $("#answers-container");
            answersContainer.html("");  // Очищаем предыдущие ответы

            for (var i = 0; i < questionData.answers.length; i++) {
                questionData.answers[i] = removeNewLines(questionData.answers[i]);
            }
            
            originalAnswersOrder = questionData.answers.slice(); // Сохраняем оригинальный порядок ответов

            switch (questionData.type) {
                case 1:
                    // Логика для радиобатонов
                    var shuffledAnswers = shuffleArray(questionData.answers);
                    for (var i = 0; i < shuffledAnswers.length; i++) {
                        answersContainer.append(`<label class="custom-radio"><input type="radio" name="answer" value="${shuffledAnswers[i]}"><span>${shuffledAnswers[i]}</span></label><br>`);
                    }
                    break;
                case 2:
                    // Логика для чекбоксов
                    var shuffledAnswers = shuffleArray(questionData.answers);
                    for (var i = 0; i < shuffledAnswers.length; i++) {
                        answersContainer.append(`<input type="checkbox" name="answer" class="custom-checkbox" id="checkbox-${i}" value="${shuffledAnswers[i]}"><label for="checkbox-${i}">${shuffledAnswers[i]}</label><br>`);
                    }
                    break;
                case 3:
                    // Логика для listbox
                    var listbox = `<select id="listbox" multiple="multiple">`;
                    for (var i = 0; i < questionData.answers.length; i++) {
                        listbox += `<option value="${questionData.answers[i]}">${questionData.answers[i]}</option>`;
                    }
                    listbox += `</select>`;
                    answersContainer.append(listbox);
                    break;
                case 6:
                    // Логика для listbox и соответствия
                    var listbox = `<select id="listbox">`;
                    for (var i = 0; i < questionData.answers.length; i++) {
                        listbox += `<option value="${i}">${questionData.answers[i].split(" ::: ")[1]}</option>`;
                    }
                    listbox += `</select>`;
                    answersContainer.append(listbox);
                    break;
                case 7:
                    // Логика для input
                    answersContainer.append(`<input type="text" id="user-answer">`);
                    break;
            }
        }

        function checkAnswer() {
            var questionData = questionsData[currentGroup].questions[currentQuestionIndex];
            var userAnswer;
            
            switch (questionData.type) {
                case 1:
                    userAnswer = $("input[name='answer']:checked").val();
                    break;
                case 2:
                    userAnswer = [];
                    $("input[name='answer']:checked").each(function() {
                        userAnswer.push($(this).val());
                    });
                    break;
                case 3:
                    userAnswer = $("#listbox").val();
                    break;
                case 6:
                    userAnswer = $("#listbox").val();
                    break;
                case 7:
                    userAnswer = $("#user-answer").val();
                    break;
            }

            // Логика проверки ответа
            var isCorrect = checkUserAnswer(questionData, userAnswer);

            // Подсветка правильного и неправильного ответов
            highlightAnswer(isCorrect);
        }

        function showAnswer() {
            var questionData = questionsData[currentGroup].questions[currentQuestionIndex];
            var correctAnswerMessage = "";

            switch (questionData.type) {
                case 1:
                    correctAnswerMessage = `<li>${originalAnswersOrder[0]}</li>`;
                    break;
                case 2:
                    var correctAnswers = originalAnswersOrder.slice(0, questionData.right);
                    correctAnswerMessage = `<ul>${correctAnswers.map(answer => `<li>${answer}</li>`).join('')}</ul>`;
                    break;
                case 3:
                case 6:
                    correctAnswerMessage = `<ul>${originalAnswersOrder.map(answer => `<li>${answer}</li>`).join('')}</ul>`;
                    break;
                case 7:
                    correctAnswerMessage = `<ul>Все ответы верные: ${originalAnswersOrder.map(answer => `<li>${answer}</li>`).join('')}</ul>`;
                    break;
            }

            // Отобразить правильный ответ
            $("#correct-answer").html(correctAnswerMessage);

            // Скрыть кнопку "Показать ответ" и показать кнопку "Скрыть ответ"
            $("button:contains('Показать ответ')").hide();
            $("button:contains('Скрыть ответ')").show();
        }

        function hideAnswer() {
            // Скрыть правильный ответ и вернуть видимость кнопки "Показать ответ"
            $("#correct-answer").text("");
            $("button:contains('Скрыть ответ')").hide();
            $("button:contains('Показать ответ')").show();
        }

        function checkUserAnswer(questionData, userAnswer) {
            console.log('questionData:')
            console.log(questionData)

            console.log('userAnswer:')
            console.log(userAnswer)

            console.log('originalAnswersOrder:')
            console.log(originalAnswersOrder)


            switch (questionData.type) {
                case 1:
                    return userAnswer === originalAnswersOrder[0];
                case 2:
                    var correctAnswers = originalAnswersOrder.slice(0, questionData.right);
                    return arraysEqual(userAnswer.sort(), correctAnswers.sort());
                case 3:
                case 6:
                    return arraysEqual(userAnswer, originalAnswersOrder.map(String));
                case 7:
                    // Проверка введенного ответа
                    return userAnswer.toLowerCase() === originalAnswersOrder[0].toLowerCase();
            }
        }

        function arraysEqual(arr1, arr2) {
            if (arr1.length !== arr2.length) return false;
            for (var i = 0; i < arr1.length; i++) {
                if (arr1[i] !== arr2[i]) return false;
            }
            return true;
        }

        function highlightAnswer(isCorrect) {
            var color = isCorrect ? "green" : "red";
            $("#answers-container").css("border", `2px solid ${color}`);
        }

        function nextQuestion() {
            // Сбросить подсветку
            $("#answers-container").css("border", "");

            // Скрыть правильный ответ и вернуть видимость кнопки "Показать ответ"
            $("#correct-answer").text("");
            $("button:contains('Скрыть ответ')").hide();
            $("button:contains('Показать ответ')").show();

            // Переход к следующему вопросу
            currentQuestionIndex++;
            if (currentQuestionIndex < questionsData[currentGroup].questions.length) {
                showQuestion();
            } else {
                // Если вопросы закончились, переход к следующей группе
                currentGroup = getNextGroup();
                if (currentGroup !== null) {
                    currentQuestionIndex = 0;
                    showQuestion();
                    $("#group-select").val(currentGroup);

                    var questionLinks = $("#question-links");
                    questionLinks.html("")
                    for (var i = 0; i < questionsData[currentGroup].questions.length; i++) {
                        questionLinks.append(`<a href="#" class="mr-1" onclick="showQuestionByIndex(${i})">${i + 1}</a>`);
                    }
                } else {
                    // Если и группы закончились, вы можете выполнить какое-то завершающее действие
                    //alert("Тест завершен!");
                }
            }
        }

        function showGroupQuestions(group) {
            // Обновить текущую группу и показать первый вопрос
            currentGroup = group;
            currentQuestionIndex = 0;
            showQuestion();
        }

        function getNextGroup() {
            // Логика получения следующей группы
            // Вернуть id следующей группы или null, если группы закончились
            var groupKeys = Object.keys(questionsData);
            var currentIndex = groupKeys.indexOf(currentGroup);
            return currentIndex < groupKeys.length - 1 ? groupKeys[currentIndex + 1] : null;
        }

        // Перемешивание массива
        function shuffleArray(array) {
            array_copy = array.slice()
            var currentIndex = array_copy.length, randomIndex;

            while (currentIndex != 0) {
                randomIndex = Math.floor(Math.random() * currentIndex);
                currentIndex--;

                [array_copy[currentIndex], array_copy[randomIndex]] = [array_copy[randomIndex], array_copy[currentIndex]];
            }

            return array_copy;
        }

        $(document).ready(function() {
            // Инициализация при загрузке страницы
            showQuestion();

            // Добавление элементов управления для выбора группы и нумерации вопросов
            var groupKeys = Object.keys(questionsData);
            var groupSelect = $("#group-select");
            for (var i = 0; i < groupKeys.length; i++) {
                groupSelect.append(`<option value="${groupKeys[i]}">${groupKeys[i]}</option>`);
            }

            groupSelect.on("change", function() {
                // При изменении выбранной группы показать первый вопрос новой группы
                showGroupQuestions($(this).val());

                var questionLinks = $("#question-links");
                questionLinks.html("")
                for (var i = 0; i < questionsData[currentGroup].questions.length; i++) {
                    questionLinks.append(`<a href="#" class="mr-1" onclick="showQuestionByIndex(${i})">${i + 1}</a>`);
                }
            });

            var questionLinks = $("#question-links");
            for (var i = 0; i < questionsData[currentGroup].questions.length; i++) {
                questionLinks.append(`<a href="#" class="mr-1" onclick="showQuestionByIndex(${i})">${i + 1}</a>`);
            }
        });

        function showQuestionByIndex(index) {
            // Показать вопрос по индексу
            currentQuestionIndex = index;
            originalAnswersOrder = null;
            showQuestion();
        }
    </script>

    <!---
    <div>
        <label for="group-select">Выберите группу: </label>
        <select id="group-select"></select>
        <div id="question-links"></div> 
    </div>
    <div id="question-container"></div>
    <div id="answers-container"></div>
    <div id="correct-answer"></div> 
    <button onclick="checkAnswer()">Ответить</button>
    <button onclick="showAnswer()">Показать ответ</button>
    <button onclick="hideAnswer()" style="display:none;">Скрыть ответ</button>
    <button onclick="nextQuestion()">Следующий вопрос</button>
    --!>


    <div class="container mt-3">
        <div class="form-group">
            <label for="group-select">Выберите группу:</label>
            <select class="form-control" id="group-select"></select>
        </div>
        <div id="question-links" class="mb-3"></div>
    </div>

    <div class="container mt-1">
        <div class="jumbotron">
            <p id="question-container"></p>
        </div>
    </div>

    <div class="container mt-3">
        <div id="answers-container"></div>
    </div>

    <div class="container text-center mt-3">
        <button class="btn btn-secondary mb-3" onclick="nextQuestion()">Следующий вопрос</button>
        <button class="btn btn-primary mb-3" onclick="checkAnswer()">Ответить</button>
    </div>

    <div class="container mt-3">
        <div class="mt-3">
            <button class="btn btn-secondary mb-3" onclick="showAnswer()">Показать ответ</button>
            <button class="btn btn-secondary mb-3" onclick="hideAnswer()" style="display:none;">Скрыть ответ</button>
        </div>
        <div id="correct-answer"></div>
    </div>

    </body>
    </html>
    """ % (json.dumps(grouped_questions))

    return html_code






            
if __name__ == "__main__":
    main()
