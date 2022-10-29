import sys
sys.path.append("..")

import os
import time
from task0.main import parse as parse_task0
from dop1.main import parse as parse_dop1
from dop2.main import parse as parse_dop2

def benchmark(function, src):
    start_time = time.time()
    for _ in range(100):
        function(src)
    print("--- %s seconds ---" % (time.time() - start_time))

if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "../data/in.json")
    string = open(input_file, "r").read()

    print("> task0...")
    benchmark(parse_task0, string)

    print("> dop1...")
    benchmark(parse_dop1, string)

    print("> dop2...")
    benchmark(parse_dop2, string)
