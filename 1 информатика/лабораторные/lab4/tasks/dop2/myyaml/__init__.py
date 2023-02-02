from enum import Enum, auto

class Type(Enum):
    NULL = auto()
    BOOL = auto()
    INT = auto()
    FLOAT = auto()
    STR = auto()
    ARR = auto()
    OBJ = auto()

    def to_type(value):
        if isinstance(value, bool):       return Type.BOOL
        elif isinstance(value, int):      return Type.INT
        elif isinstance(value, float):    return Type.FLOAT
        elif isinstance(value, str):      return Type.STR
        elif isinstance(value, type([])): return Type.ARR
        elif isinstance(value, type({})): return Type.OBJ
        return Type.NULL


class Yaml:
    def dump(obj):
        return "---\n" + Yaml.create(obj).to_string()

    def __init__(self, data_type, data):
        self._data, self._data_type = data, data_type

    def create(obj):
        data_type = Type.to_type(obj)
        data = obj
        if data_type == Type.ARR:
            data = [Yaml.create(val) for val in obj]
        elif data_type == Type.OBJ:
            data = dict((key, Yaml.create(val))
                        for (key, val) in obj.items())
        return Yaml(data_type, data)

    def to_string(self, tabs=0, prefix=''):
        def pre(tabs):
            return "  " * tabs

        if ((self._data_type == Type.ARR or self._data_type == Type.OBJ) and len(self._data) == 0):
            return "[]" if self._data_type == Type.ARR else "{}"
        elif self._data_type == Type.ARR:
            return '' if tabs == 0 else '\n' + '\n'.join(
                ["{}- {}".format(pre(tabs - 1),
                                 val.to_string(tabs))
                 for val in self._data]) + '\n'
        elif self._data_type == Type.OBJ:
            return '\n{}'.format(pre(tabs)).join(
                ["{}:\n{}{}".format(key, pre(tabs + 1), val.to_string(tabs + 1, prefix=' ')) if (val._data_type == Type.OBJ) else "{}:{}".format(key, val.to_string(tabs + 1, prefix=' '))
                    for (key, val) in self._data.items()])
        elif self._data_type == Type.NULL:
            return prefix + "null"
        elif self._data_type == Type.STR:
            if len(self._data.split('\n')) > 1:
                return " |\n" + '\n'.join(pre(tabs + 1) + val
                                          for val in self._data.split('\n'))
            else:
                return prefix + self._data
        else:
            return prefix + str(self._data)
