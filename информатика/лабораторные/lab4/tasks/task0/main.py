import sys
sys.path.append("..")

import os
from task0.myyaml import Yaml as yaml
import task0.myjson as json

def parse(string):
    return yaml.dump(json.loads(string))

if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "../data/in.json")
    output_file = os.path.join(os.path.dirname(__file__), "../data/out.yaml")

    string = open(input_file, "r").read()
    open(output_file, "w").write(parse(string))
    print("task0.main complete!")
