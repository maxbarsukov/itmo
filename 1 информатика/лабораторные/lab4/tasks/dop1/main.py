import os
import sys
import yaml
import json

def parse(string):
    return yaml.dump(json.loads(string), default_flow_style=False, allow_unicode=True, sort_keys=False)

if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "../data/in.json")
    output_file = os.path.join(os.path.dirname(__file__), "../data/out-dop1.yaml")

    string = open(input_file, "r").read()
    open(output_file, "w").write(parse(string))
    print("dop1.main complete!")
