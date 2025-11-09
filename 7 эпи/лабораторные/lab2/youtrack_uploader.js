/*
MIT License

Copyright (c) 2025 Dmitry Tsenkov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

// Данный скрипт предназначен для импорта CSV в Канбан youtrack.
(()=>{
    // Сюда напиши свой домен!
    const DOMAIN = 'https://DOMAIN.youtrack.cloud/';

    // Профиль -> Безопасность аккаунта -> Токены -> Новый токен...
    const ACCESS_TOKEN = 'TOKEN';

    // Название проекта
    const PROJECT = 'PROJECT';

    // Проект -> Учет времени -> включить
    // Проект -> Учет времени -> Поле оценки -> Estimation
    // Проект -> Учет времени -> Затраченное время -> Spent Time
    // Имя пользователя от имени кого будет проставляться время в задачах
    const PM = 'admin';

    // Кроме Summary и Description поля должны быть созданы в Проект->Настраиваемые поля.
    // Они должны должным образом настроены и иметь нужные значения.
    // Поле Sprint должно быть привязано к Доскам Agile (Проект->Доски Agile->Параметры доски->Поведение доски->Связывать спринты со значениями поля Sprint)
    //
    // Поддерживаемые типы полей этим скриптом:
    //   дата, дата и время, перечисление, состояние, строковое значение, пользователь, период
    const CSV = `Summary,Description,Assignee,Priority,Stage,Sprint,Start Date,Due Date,Estimation,Spent Time,Tags,Components
"Интервью со стейкхолдерами","Встречи с заказчиком, редакцией, бизнес-аналитиками",admin,Critical,Done,"Спринт 1 - Старт и планирование",2025-11-05,2025-11-06,8h,9h,"анализ,требования","Анализ"
PLACE_YOUR_DATA_HERE
PLACE_YOUR_DATA_HERE
PLACE_YOUR_DATA_HERE
PLACE_YOUR_DATA_HERE
`.replace(/^\s+/gm, '');
    console.log(parseCSV(CSV));

    const CHECK_NO_ISSUES = true;

    async function DO_IT(arrayOfIssues) {
        // https://www.jetbrains.com/help/youtrack/devportal/HUB-REST-API_Users_Get-All-Users.html
        const users = await fetchJSON('GET', `users?fields=id,ringId,name`);
        const user_PM = users.find(user => user.name === PM);
        if (!user_PM)
            throw new Error(`PM не найден`);

        // https://www.jetbrains.com/help/youtrack/devportal/resource-api-issues.html#get_all-Issue-method
        const issues = (await fetchJSON('GET', 'issues?fields=id,summary,project(name,id)')).filter(x=>x.project.name==PROJECT);
        if (CHECK_NO_ISSUES && issues.length)
            throw new Error('У вас уже есть задачи в проекте. Либо удалите задачи, которые есть в проекте, либо установите CHECK_NO_ISSUES = true в значение false');

        // https://www.jetbrains.com/help/youtrack/devportal/HUB-REST-API_Projects_Get-All-Projects.html
        const project = (await fetchJSON('GET', 'admin/projects?fields=id,name')).filter(x=>x.name==PROJECT)[0];
        if (!project)
            throw new Error(`Не найден проект с названием ${PROJECT}`);

        const ignoreFields = ['Summary', 'Description'];

        // https://www.jetbrains.com/help/youtrack/devportal/resource-api-admin-customFieldSettings-customFields.html#get_all-CustomField-method
        const customFields = await fetchJSON('GET', `admin/projects/${project.id}/fields?fields=id,field(id,name,fieldType(id,isMultiValue)),bundle(id,values(id,name,$type)),$type`);
        if (!arrayOfIssues.length)
            throw new Error(`arrayOfIssues не содержит задач для импорта`);
        else {
            const notFoundFields = Object.keys(arrayOfIssues[0]).filter(x=>![...ignoreFields, ...customFields.map(x=>x.field.name)].includes(x));
            if (notFoundFields.length) throw new Error(`Не найдены пользовательские поля задач: `+notFoundFields.join(', '));
        }

        const customFieldsChecker = customFields.map(x => {
            const fieldType = x.field.fieldType.id;
            const proxy_data = {
                name: x.field.name,
                id: x.id,
                fieldType,
                isMultiValue: x.field.fieldType.isMultiValue,
                $type: x.$type
            };

            if (fieldType === "period" || fieldType === "period[*]")
                return {
                    converter: value => (/^(\d+[ndhmгмдчмс] )*(\d+[ndhmгмдчмс])$/).test(value) ? {presentation: value} : false,
                    ...proxy_data,
                };

            if (fieldType === "date" || fieldType === "date[*]")
                return {
                    ...proxy_data,
                    converter: value => {
                        const date = new Date(value);
                        return isNaN(date.getTime()) ? false : date.getTime();
                    }
                };

            if (fieldType === "date and time" || fieldType === "date and time[*]")
                return {
                    ...proxy_data,
                    converter: value => {
                        const date = new Date(value);
                        return isNaN(date.getTime()) ? false : date.getTime();
                    }
                };

            if (fieldType === "enum[1]" || fieldType === "enum[*]")
                return {
                    ...proxy_data,
                    converter: value => {
                        if (!x.bundle || !x.bundle.values) return false;

                        const foundValue = x.bundle.values.find(item =>
                            item.name.toLowerCase() === value.toLowerCase() ||
                            item.id === value
                        );
                        return foundValue ? {id: foundValue.id, name: foundValue.name, $type: foundValue.$type} : false;
                    },
                    "$type":(proxy_data.isMultiValue?'Multi':'Single')+proxy_data.$type
                };

            if (fieldType === "state[1]" || fieldType === "state[*]")
                return {
                    ...proxy_data,
                    converter: value => {
                        if (!x.bundle || !x.bundle.values) return false;

                        const foundValue = x.bundle.values.find(item =>
                            item.name.toLowerCase() === value.toLowerCase() ||
                            item.id === value
                        );
                        return foundValue ? {id: foundValue.id, name: foundValue.name, $type: foundValue.$type} : false;
                    }
                };

            if (fieldType === "user[1]" || fieldType === "user[*]")
                return {
                    ...proxy_data,
                    converter: value => {
                        const foundUser = users.find(user => user.name === value);
                        return foundUser ? { ringId: foundUser.ringId } : false;
                    },
                    "$type":(proxy_data.isMultiValue?'Multi':'Single')+proxy_data.$type
                };

            if (fieldType === "string" || fieldType === "string[*]")
                return {
                    ...proxy_data,
                    converter: value => value
                };

            return {name: x.field.name, fieldType};
        });

        for (const f of customFieldsChecker) {
            if (!f) console.log("WARN: ._.");
            if (!f.converter) console.log(`WARN: Неизвестный тип поля ${f.name}: ${f.fieldType}`);
        }

        for (const issue of arrayOfIssues) {
            const customFieldsData = [];

            for (const fieldName in issue) {
                if (ignoreFields.includes(fieldName)) continue;

                const fieldChecker = customFieldsChecker.find(f => f.name === fieldName);
                if (!fieldChecker) throw new Error("._.");
                if (!fieldChecker.converter) throw new Error(`Неизвестный тип поля ${fieldChecker.name}: ${fieldChecker.fieldType}`);

                const value = issue[fieldName];
                let convertedValue;
                if (fieldChecker.isMultiValue) {
                    convertedValue = processMultiValueField(value, fieldChecker.converter, );
                } else {
                    convertedValue = fieldChecker.converter(value);
                }

                if (convertedValue !== false) {
                    customFieldsData.push({"value": convertedValue, "name": fieldChecker.name, "$type": fieldChecker.$type.replace('Project','Issue'), "id": fieldChecker.id});
                } else {
                    throw new Error("Для задачи "+JSON.stringify(issue)+' поле '+fieldName+' с типом '+fieldChecker.fieldType+' не было разобрано');
                }
            }

            issue._fields_data = customFieldsData;
        }

        console.log(arrayOfIssues);
        let incr = 0;
        for (const issue of arrayOfIssues) {
            const createdIssue = await fetchJSON('POST','issues', {
                "project": {"id": project.id},
                "summary": issue.Summary,
                "description": issue.Description,
                "customFields": issue._fields_data.filter((x,i)=>x.name!='Spent Time')
            });
            await fetchJSON('POST',`issues/${createdIssue.id}/timeTracking/workItems/`, {
                attributes: [],
                author:{ringId: user_PM.ringId},
                creator:{ringId: user_PM.id},
                date: +new Date(issue._fields_data.filter((x,i)=>x.name=='Due Date')[0].value),
                duration: issue._fields_data.filter((x,i)=>x.name=='Spent Time')[0].value,
                text: '',
                type: null
            });
            console.log((++incr) + '/' + arrayOfIssues.length);
        }
        console.log("Успешно!!!");
    }

    DO_IT(parseCSV(CSV));

    async function fetchJSON(method, url, body) {
        const r = await fetch(DOMAIN+'api/'+url, {
            method: method,
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Bearer '+ACCESS_TOKEN,
                'Cache-Control': 'no-cache',
                'Content-Type': 'application/json'
            },
            body: body && JSON.stringify(body)
        }).catch(_=>{throw new Error(`CORS? Run it in NODE.JS or DevTool on ${DOMAIN}!`);});
        if (!r.ok) {
            let err;
            try{
                err = await r.json();
            } catch (e){};
            throw new Error(`ERROR (${method} ${DOMAIN+'api/'+url}) ${r.status}`+(err?': '+JSON.stringify(err):''));
        }
        return await r.json();
    }

    function periodToMilliseconds(period) {
        const match = period.match(/(\d+)([hmd])/);
        if (!match) return 0;

        const value = parseInt(match[1]);
        const unit = match[2];

        switch (unit) {
            case 'h': return value * 60 * 60 * 1000;
            case 'm': return value * 60 * 1000;
            case 'd': return value * 24 * 60 * 60 * 1000;
            default: return 0;
        }
    }

    function processMultiValueField(value, converter) {
        const values = value.split(',').map(v => v.trim());
        const convertedValues = values.map(v => converter(v)).filter(v => v !== false);
        return convertedValues.length > 0 ? convertedValues : false;
    }

    function parseCSV(csv) {
        const lines = CSVToArray(csv,',');
        const headers = lines[0];
        const result = [];

        for (let i = 1; i < lines.length; i++) {
            const obj = {};
            const currentline = lines[i];

            for (let j = 0; j < headers.length; j++) {
                obj[headers[j]] = currentline[j];
            }
            result.push(obj);
        }
        return result;
    }

    function CSVToArray(strData, strDelimiter){
        strDelimiter = (strDelimiter || ",");
        var objPattern = new RegExp("(\\" + strDelimiter + "|\\r?\\n|\\r|^)(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|([^\"\\" + strDelimiter + "\\r\\n]*))", "gi");
        var arrData = [[]];
        var arrMatches = null;
        while (arrMatches = objPattern.exec(strData)) {
            var strMatchedDelimiter = arrMatches[1];
            if (strMatchedDelimiter.length && strMatchedDelimiter !== strDelimiter) arrData.push([]);
            var strMatchedValue;
            if (arrMatches[2])
                strMatchedValue = arrMatches[2].replace(new RegExp( "\"\"", "g" ), "\"");
            else
                strMatchedValue = arrMatches[3];
            arrData[arrData.length - 1].push(strMatchedValue);
        }
        return arrData;
    }
})();
