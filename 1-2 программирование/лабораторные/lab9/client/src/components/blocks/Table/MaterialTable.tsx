import React, { useState } from 'react';
import Product from 'interfaces/models/Product';
import MaterialTable, { Column } from '@material-table/core';
import { ExportCsv, ExportPdf } from '@material-table/exporters';
import { AddBox, Edit, DeleteSweep, DeleteOutline } from '@material-ui/icons';
import { useSelector } from 'hooks';
import { darken, lighten, makeStyles } from '@material-ui/core/styles';
import { MAX_WIDTH, MIN_WIDTH } from 'config/constants';
import isDarkTheme from 'utils/isDarkTheme';
import { useTheme } from '@material-ui/core';
import ProductCell, { productToCell } from 'interfaces/ProductCell';
import { useSnackbar } from 'notistack';
import { useTranslation } from 'react-i18next';
import ruLocale from 'date-fns/locale/ru';

import DeleteMenu from './modals/Delete';
import AddOrEditMenu from './modals/AddOrEdit';
import ClearMenu from './modals/Clear';

declare module '@material-table/core' {
  // eslint-disable-next-line @typescript-eslint/ban-types
  interface Column<RowData extends object> {
    maxWidth?: number;
  }
}

const useStyles = makeStyles(theme => ({
  container: {
    overflowX: 'initial',
    overflow: 'auto',
    marginBottom: theme.spacing(4),
    wordWrap: 'break-word',
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
}));

const Table = ({ data }: { data: Product[] }) => {
  const classes = useStyles();
  const theme = useTheme();
  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const userData = useSelector(state => state.auth.user);
  const isLoggedIn = useSelector(store => store.auth.isLoggedIn);

  const [columns] = useState<Column<ProductCell>[]>([
    {
      field: 'id',
      title: t('pages.Home.Table.columns.id'),
      align: 'right',
      resizable: true,
      minWidth: 100,
    },
    {
      field: 'name',
      title: t('pages.Home.Table.columns.name'),
      align: 'right',
      minWidth: 200,
      maxWidth: 200,
    },
    {
      field: 'x',
      title: t('pages.Home.Table.columns.x'),
      type: 'numeric',
      align: 'right',
    },
    {
      field: 'y',
      title: t('pages.Home.Table.columns.y'),
      type: 'numeric',
      align: 'right',
    },
    {
      field: 'creationDate',
      title: t('pages.Home.Table.columns.creationDate'),
      type: 'date',
      align: 'right',
    },
    {
      field: 'price',
      title: t('pages.Home.Table.columns.price'),
      type: 'numeric',
      align: 'right',
    },
    {
      field: 'partNumber',
      title: t('pages.Home.Table.columns.partNumber'),
      align: 'right',
      minWidth: 200,
      maxWidth: 200,
    },
    {
      field: 'unitOfMeasure',
      title: t('pages.Home.Table.columns.unitOfMeasure'),
      align: 'right',
    },
    {
      field: 'manufacturerId',
      title: t('pages.Home.Table.columns.manufacturerId'),
      type: 'numeric',
      align: 'right',
    },
    {
      field: 'manufacturerName',
      title: t('pages.Home.Table.columns.manufacturerName'),
      align: 'right',
      minWidth: 100,
      maxWidth: 100,
    },
    {
      field: 'manufacturerEmployeesCount',
      title: t('pages.Home.Table.columns.manufacturerEmployeesCount'),
      type: 'numeric',
      align: 'right',
    },
    {
      field: 'manufacturerType',
      title: t('pages.Home.Table.columns.manufacturerType'),
      align: 'right',
    },
    {
      field: 'manufacturerStreet',
      title: t('pages.Home.Table.columns.manufacturerStreet'),
      align: 'right',
      minWidth: 200,
      maxWidth: 200,
    },
    {
      field: 'manufacturerZipCode',
      title: t('pages.Home.Table.columns.manufacturerZipCode'),
      align: 'right',
      minWidth: 100,
      maxWidth: 100,
    },
    {
      field: 'creatorId',
      title: t('pages.Home.Table.columns.creatorId'),
      type: 'numeric',
      align: 'right',
    },
    {
      field: 'creatorName',
      title: t('pages.Home.Table.columns.creatorName'),
      align: 'right',
      minWidth: 100,
      maxWidth: 100,
    },
  ]);
  const [rowData, setRowData] = useState<ProductCell>();

  const orderCollection = [
    { orderBy: 0, orderDirection: 'asc', sortOrder: 0 },
  ];

  const [isDeleteMenuOpen, setDeleteMenuOpen] = useState(false);
  const [isClearMenuOpen, setClearMenuOpen] = useState(false);
  const [isAddOrEditMenuOpen, setAddOrEditMenuOpen] = useState(false);

  const openDeleteMenu = () => setDeleteMenuOpen(true);
  const openClearMenu = () => setClearMenuOpen(true);
  const openAddOrEditMenu = () => setAddOrEditMenuOpen(true);

  const editable = data.map(o => ({ ...o })).map(productToCell);
  return (
    <div className={classes.container}>
      <MaterialTable
        title={t('pages.Home.Table.title')}
        columns={columns}
        data={editable}
        renderSummaryRow={({ column, data }) => {
          if (column.field === 'id') {
            return {
              value: `${t('pages.Home.Table.columns.summary')}: `,
              style: {
                textTransform: 'uppercase',
                fontWeight: 500,
              },
            };
          }
          if (column.field === 'price') {
            return {
              value: 'Σ: ' + data.reduce((agg, row) => agg + row.price, 0),
            };
          }
        }}
        options={{
          exportMenu: [{
            label: t('pages.Home.Table.export.pdf'),
            exportFunc: (cols, data) =>
              ExportPdf(cols, data, 'ProductsPDF'),
          }, {
            label: t('pages.Home.Table.export.csv'),
            exportFunc: (cols, data) =>
              ExportCsv(cols, data, 'ProductsCSV'),
          }],
          rowStyle: (rowData, index) => {
            const styles: Record<string, unknown> = {};
            if (index % 2 === 0) {
              styles.background = lighten(theme.palette.background.default, 0.03);
            }
            return styles;
          },
          tableWidth: 'variable',
          grouping: true,
          search: true,
          searchFieldAlignment: 'right',
          searchAutoFocus: true,
          maxColumnSort: 3,
          defaultOrderByCollection: orderCollection,
          filtering: true,
          draggable: true,
          searchFieldVariant: 'standard',
          showEmptyDataSourceMessage: true,
          paginationType: 'stepped',
          emptyRowsWhenPaging: false,
          paginationPosition: 'both',
          numberOfPagesAround: 2,
          showFirstLastPageButtons: true,
          pageSize: 10,
          pageSizeOptions: [5, 10, 20, 50],
          padding: 'dense',
        }}
        localization={{
          pagination: {
            labelDisplayedRows: `{from}-{to} ${t('pages.Home.Table.pagination.labelDisplayedRows')} {count}`,
            labelRowsPerPage: t('pages.Home.Table.pagination.labelRowsPerPage'),
            labelRowsSelect: t('pages.Home.Table.pagination.labelRowsSelect'),
            firstAriaLabel: t('pages.Home.Table.pagination.firstAriaLabel'),
            firstTooltip: t('pages.Home.Table.pagination.firstTooltip'),
            previousAriaLabel: t('pages.Home.Table.pagination.previousAriaLabel'),
            previousTooltip: t('pages.Home.Table.pagination.previousTooltip'),
            nextAriaLabel: t('pages.Home.Table.pagination.nextAriaLabel'),
            nextTooltip: t('pages.Home.Table.pagination.nextTooltip'),
            lastAriaLabel: t('pages.Home.Table.pagination.lastAriaLabel'),
            lastTooltip: t('pages.Home.Table.pagination.lastTooltip'),
          },
          header: {
            actions: t('pages.Home.Table.header.actions'),
          },
          grouping: {
            groupedBy: t('pages.Home.Table.grouping.groupedBy'),
            placeholder: t('pages.Home.Table.grouping.placeholder'),
          },
          body: {
            dateTimePickerLocalization: ruLocale,
            emptyDataSourceMessage: t('pages.Home.Table.emptyDataSourceMessage'),
            filterRow: {
              filterTooltip: t('pages.Home.Table.filter.tooltip'),
            },
          },
          toolbar: {
            addRemoveColumns: t('pages.Home.Table.toolbar.addRemoveColumns'),
            nRowsSelected: `${t('pages.Home.Table.toolbar.nRowsSelected')} {0}`,
            showColumnsTitle: t('pages.Home.Table.toolbar.showColumnsTitle'),
            showColumnsAriaLabel: t('pages.Home.Table.toolbar.showColumnsAriaLabel'),
            exportTitle: t('pages.Home.Table.toolbar.exportTitle'),
            exportAriaLabel: t('pages.Home.Table.toolbar.exportAriaLabel'),
            exportCSVName: t('pages.Home.Table.toolbar.exportCSVName'),
            exportPDFName: t('pages.Home.Table.toolbar.exportPDFName'),
            searchTooltip: t('pages.Home.Table.toolbar.searchTooltip'),
            searchPlaceholder: t('pages.Home.Table.toolbar.searchPlaceholder'),
            searchAriaLabel: t('pages.Home.Table.toolbar.searchAriaLabel'),
            clearSearchAriaLabel: t('pages.Home.Table.toolbar.clearSearchAriaLabel'),
          },
        }}
        actions={[
          // Edit
          rowData => ({
            icon: () =>
              <Edit color={rowData.creatorId !== userData?.id ? 'disabled' : 'primary'} />,
            tooltip: t('pages.Home.Table.actions.edit'),
            onClick: (event, rowData) => {
              const data = rowData as ProductCell;
              if (!isLoggedIn) {
                enqueueSnackbar(t('pages.Home.Table.errors.notLoggedIn'), {
                  variant: 'error', autoHideDuration: 4000,
                });
                return;
              }
              if (data.creatorId !== userData?.id) {
                enqueueSnackbar(t('pages.Home.Table.errors.badOwner'), {
                  variant: 'error', autoHideDuration: 2000,
                });
                return;
              }
              openAddOrEditMenu();
              setRowData(data);
            },
          }),
          // Delete
          rowData => ({
            icon: () =>
              <DeleteOutline color={rowData.creatorId !== userData?.id ? 'disabled' : 'primary'} />,
            tooltip: t('pages.Home.Table.actions.delete'),
            onClick: (event, rowData) => {
              const data = rowData as ProductCell;
              if (!isLoggedIn) {
                enqueueSnackbar(t('pages.Home.Table.errors.notLoggedIn'), {
                  variant: 'error', autoHideDuration: 4000,
                });
                return;
              }
              if (data.creatorId !== userData?.id) {
                enqueueSnackbar(t('pages.Home.Table.errors.badOwner'), {
                  variant: 'error', autoHideDuration: 2000,
                });
                return;
              }
              openDeleteMenu();
              setRowData(data);
            },
          }),
          // Add
          {
            icon: () => <AddBox color={!isLoggedIn ? 'disabled' : 'primary'} />,
            tooltip: t('pages.Home.Table.actions.add'),
            isFreeAction: true,
            onClick: () => {
              if (!isLoggedIn) {
                enqueueSnackbar(t('pages.Home.Table.errors.notLoggedIn'), {
                  variant: 'error', autoHideDuration: 4000,
                });
                return;
              }
              openAddOrEditMenu();
              setRowData(null);
            },
          },
          // Clear
          {
            icon: () => <DeleteSweep color={!isLoggedIn ? 'disabled' : 'primary'} />,
            tooltip: t('pages.Home.Table.actions.clear'),
            isFreeAction: true,
            onClick: () => {
              if (!isLoggedIn) {
                enqueueSnackbar(t('pages.Home.Table.errors.notLoggedIn'), {
                  variant: 'error', autoHideDuration: 4000,
                });
                return;
              }
              openClearMenu();
            },
          },
        ]}
      />
      {isDeleteMenuOpen &&
        <DeleteMenu productId={rowData ? rowData.id : null} isOpen={isDeleteMenuOpen} setOpen={setDeleteMenuOpen} />
      }
      { isAddOrEditMenuOpen &&
        <AddOrEditMenu
          productId={rowData?.id}
          isOpen={isAddOrEditMenuOpen}
          setOpen={setAddOrEditMenuOpen}
        />
      }
      {isClearMenuOpen &&
        <ClearMenu isOpen={isClearMenuOpen} setOpen={setClearMenuOpen} />
      }
    </div>
  );
};

export default React.memo(Table);
