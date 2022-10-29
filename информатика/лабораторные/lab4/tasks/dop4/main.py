import os
import yaml
import json

import generated.dop4_pb2 as schedule

def convert(obj):
    s = schedule.Schedule()
    s.even_week = obj['even_week']
    s.day_id = obj['day_id']
    s.group_name = obj['group_name']

    for i in range(len(obj['schedule'])):
        item = schedule.Schedule.ScheduleItem()
        current = obj['schedule'][i]

        item.id = current['id']
        item.title = current['title']
        item.class_format = current['class_format']

        item_type = schedule.Schedule.ScheduleItem.Type()
        item_type.id = current['type']['id']
        item_type.title = current['type']['title']
        item.type.CopyFrom(item_type)

        item_campus = schedule.Schedule.ScheduleItem.Campus()
        if current['campus']['id'] == 0:
            item_campus.id = schedule.Schedule.ScheduleItem.Campus.CampusId.GK
        else:
            item_campus.id = schedule.Schedule.ScheduleItem.Campus.CampusId.LOMO

        item_campus.address = current['campus']['address']
        item.campus.CopyFrom(item_campus)

        item_time = schedule.Schedule.ScheduleItem.Time()
        item_time.period = current['time']['period']
        item_time.from_time = current['time']['from_time']
        item_time.to_time = current['time']['to_time']
        item.time.CopyFrom(item_time)

        auditory_type = schedule.Schedule.ScheduleItem.Auditory()
        auditory_type.id = current['auditory']['id']
        auditory_type.name = current['auditory']['name']
        item.auditory.CopyFrom(auditory_type)

        teacher_type = schedule.Schedule.ScheduleItem.Teacher()
        teacher_type.id = current['teacher']['id']
        teacher_type.name = current['teacher']['name']
        item.teacher.CopyFrom(teacher_type)

        s.schedule.append(item)

    return s

if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "../data/in.json")
    output_file = os.path.join(os.path.dirname(__file__), "../data/out-dop4")

    data = json.loads(open(input_file, "r").read())
    result = convert(data)

    open(output_file, "wb").write(result.SerializeToString())
    print("dop4.main complete!")        
