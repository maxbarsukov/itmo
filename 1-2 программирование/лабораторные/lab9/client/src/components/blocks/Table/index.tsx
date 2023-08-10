import React from 'react';
import Product from 'interfaces/models/Product';
import {
  createColumnHelper,
  flexRender,
  getCoreRowModel,
  useReactTable,
} from '@tanstack/react-table';

import {
  Table as MuiTable,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from '@material-ui/core';
import { darken, lighten, makeStyles } from '@material-ui/core/styles';

import isDarkTheme from 'utils/isDarkTheme';
import { MAX_WIDTH, MIN_WIDTH } from 'config/constants';

const useStyles = makeStyles(theme => ({
  container: {
    overflowX: 'initial',
    overflow: 'auto',
    marginBottom: theme.spacing(4),
    [theme.breakpoints.down(MIN_WIDTH)]: {
      height: 'auto',
    },
    [theme.breakpoints.up(MIN_WIDTH)]: {
      height: window.innerHeight * 0.75,
    },
    [theme.breakpoints.up(MAX_WIDTH)]: {
      height: 'auto',
    },
    '&::-webkit-scrollbar': {
      width: 10,
      height: 10,
      background: isDarkTheme(theme)
        ? lighten(theme.palette.background.default, 0.03)
        : theme.palette.background.paper,
      border: `1px solid ${darken(theme.palette.background.paper, 0.05)}`,
    },
    '&::-webkit-scrollbar-corner': {
      background: isDarkTheme(theme)
        ? lighten(theme.palette.background.default, 0.03)
        : theme.palette.background.paper,
    },
    '&::-webkit-scrollbar-thumb': {
      minHeight: 20,
      borderRadius: theme.shape.borderRadius,
      background: isDarkTheme(theme)
        ? lighten(theme.palette.background.paper, 0.08)
        : darken(theme.palette.background.paper, 0.08),
      transition: '0.1s',
      '&:hover': {
        background: isDarkTheme(theme)
          ? lighten(theme.palette.background.paper, 0.1)
          : darken(theme.palette.background.paper, 0.1),
      },
      '&:active': {
        background: isDarkTheme(theme)
          ? lighten(theme.palette.background.paper, 0.2)
          : darken(theme.palette.background.paper, 0.2),
      },
    },
  },
  table: {
    fontFamily: 'Segoe UI',
    wordWrap: 'break-word',
    maxWidth: 'auto',
    width: 'auto',
    height: 'max-content',
  },
  sticky: {
    fontWeight: 'bold',
    position: 'sticky',
    left: 0,
    outline: `0.5px solid ${theme.palette.divider}`,
    boxShadow: `0 0 0 1px ${theme.palette.divider}`,
  },
  darkerCell: {
    background: lighten(theme.palette.background.default, 0.03),
  },
  numberCell: {
    color: theme.palette.primary.light,
  },
  enumCell: {
    fontWeight: 'bold',
    color: theme.palette.success.main + ' !important',
  },
  nameCell: {
    fontWeight: 'bold',
    color: theme.palette.warning.light + ' !important',
  },
  resizer: {
    position: 'absolute',
    right: 0,
    top: 0,
    height: '100%',
    width: '5px',
    background: 'red',
    cursor: 'col-resize',
    userSelect: 'none',
    touchAction: 'none',
    opacity: 0,
    '&:hover': {
      opacity: 1,
    },
  },
  isResizing: {
    background: 'blue',
    opacity: 1,
  },
}));

const Table = ({ data }: { data: Product[] }) => {
  const classes = useStyles();

  const columnHelper = createColumnHelper<Product>();

  const columns = [
    columnHelper.accessor('id', {
      header: 'ID',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor('name', {
      header: 'Name',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor('x', {
      header: 'X',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor('y', {
      header: 'Y',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor('creationDate', {
      header: 'Creation Date',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor('price', {
      header: 'Price',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor('partNumber', {
      header: 'Part Number',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor('unitOfMeasure', {
      header: 'Unit Of Measure',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor(({ manufacturer }) => manufacturer?.id, {
      header: 'Manufacturer ID',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor(({ manufacturer }) => manufacturer?.name, {
      header: 'Manufacturer Name',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor(({ manufacturer }) => manufacturer?.employeesCount, {
      header: 'Manufacturer Employees Count',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor(({ manufacturer }) => manufacturer?.type, {
      header: 'Manufacturer Type',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor(({ manufacturer }) => manufacturer?.street, {
      header: 'Manufacturer Street',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor(({ manufacturer }) => manufacturer?.zipCode, {
      header: 'Manufacturer Zip-code',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor(({ creator }) => creator?.id, {
      header: 'Creator ID',
      cell: info => info?.getValue() || null,
    }),
    columnHelper.accessor(({ creator }) => creator?.name, {
      header: 'Creator Name',
      cell: info => info?.getValue() || null,
    }),
  ];

  const table = useReactTable({
    data,
    columns,
    columnResizeMode: 'onChange',
    getCoreRowModel: getCoreRowModel(),
  });

  return (
    <TableContainer component={Paper} className={classes.container}>
      <MuiTable className={classes.table} size="small">
        <TableHead>
            {table.getHeaderGroups().map(headerGroup => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header, i) => (
                  <TableCell
                    key={header.id}
                    colSpan={header.colSpan}
                    style={{
                      width: header.getSize(),
                    }}
                    className={`${i === 0 ? classes.sticky : ''} ${i % 2 === 0 ? classes.darkerCell : ''}`}
                    align="right"
                  >
                    {header.isPlaceholder
                      ? null
                      : flexRender(
                        header.column.columnDef.header,
                        header.getContext()
                      )}
                    <div onMouseDown={header.getResizeHandler()}
                         onTouchStart={header.getResizeHandler()}
                         className={`${classes.resizer} ${header.column.getIsResizing() ? classes.isResizing : ''}`}
                    />
                  </TableCell>
                ))}
              </TableRow>
            ))}
        </TableHead>
        <TableBody>
          {table.getRowModel().rows.map(row => (
            <TableRow key={row.id}>
              {row.getVisibleCells().map((cell, i) => (
                <TableCell
                  key={cell.id}
                  style={{
                    width: cell.column.getSize(),
                  }}
                  className={`${i === 0 ? classes.sticky : ''} \
                   ${i % 2 === 0 ? classes.darkerCell : ''} \
                   ${[1, 9, 15].includes(i) ? classes.nameCell : ''} \
                   ${[2, 3, 5, 8, 10, 14].includes(i) ? classes.numberCell : ''} \
                   ${[7, 11].includes(i) ? classes.enumCell : ''}`}
                  align="right"
                >
                  {flexRender(cell.column.columnDef.cell, cell.getContext())}
                </TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </MuiTable>
    </TableContainer>
  );
};

export default React.memo(Table);
