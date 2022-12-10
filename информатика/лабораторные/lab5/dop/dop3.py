from datetime import date
import plotly.graph_objs as go
import plotly.express as px
from plotly.subplots import make_subplots

import numpy as np
import pandas as pd
import csv

with open('data.csv') as file:  
    reader = csv.reader(file, delimiter=",", quotechar='"')
    next(reader, None)
    data_read = [row for row in reader]

d_open  = [[], [] ,[], []]
d_high  = [[], [] ,[], []]
d_low   = [[], [] ,[], []]
d_close = [[], [] ,[], []]

dates = { '17/09/18': 0, '17/10/18': 1, '19/11/18': 2, '17/12/18': 3 }
inv_dates = {v: k for k, v in dates.items()}

for raw in data_read:
    id = dates[raw[2]]
    d_open[id].append(float(raw[4]))
    d_high[id].append(float(raw[5]))
    d_low[id].append(float(raw[6]))
    d_close[id].append(float(raw[7]))

fig = go.Figure()
for i in range(4):
    cur_date = inv_dates[i]
    n = cur_date + ' - Open'
    fig.add_trace(go.Box(y=pd.DataFrame(d_open[i], columns=[n])[n], name=n))

    n = cur_date + ' - High'
    fig.add_trace(go.Box(y=pd.DataFrame(d_high[i], columns=[n])[n], name=n))

    n = cur_date + ' - Low'
    fig.add_trace(go.Box(y=pd.DataFrame(d_low[i], columns=[n])[n], name=n))

    n = cur_date + ' - Close'
    fig.add_trace(go.Box(y=pd.DataFrame(d_close[i], columns=[n])[n], name=n))

fig.show()
