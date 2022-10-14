from tasks import task1, task2, task3

task = input("Enter task number (1-3) you want to run: ")
if task == '1':
    print(task1.solve(input("Enter your string to find the number of emoticons: ")))
elif task == '2':
    print(task2.solve(input("Enter your string to find duplicated words: ")))
elif task == '3':
    print(task3.solve(input("Enter your string to find words with one vowel letter: ")))
else:
    print("Can't resolve task number")
