import json
from numpy import var
import requests

FIND_A = '0E9D'
FIND_B = 'F07E'
FIND_C = 'D345'
VARAINTS = range(10000)

for variant in VARAINTS:
    url = f'https://se.ifmo.ru/web/guest/service/csbasics/-/generator/test2/train/{variant}'
    response = requests.get(url)
    print(f'- Checking varint {variant}...')


    if response:
        data = json.loads(response.content)
        var_a = data['cmds'][0][1]
        var_b = data['cmds'][1][1]
        var_c = data['cmds'][2][1]
        print('    A, B, C: ' + var_a, var_b, var_c)

        if var_a == FIND_A or var_b == FIND_B or var_c == FIND_C:
            print(f'--- Found: Variant {variant}')
            input()
    else:
        print(f'Variant {variant}: An error has occurred. Status: {response.status_code}')
 