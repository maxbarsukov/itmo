import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { TransitionProps } from '@material-ui/core/transitions';

import {
  Backdrop,
  Button,
  CircularProgress,
  Grid,
  Dialog,
  DialogTitle,
  DialogContentText,
  DialogContent,
  DialogActions,
  Fade,
  IconButton,
  TextField,
  Typography,
  PaperProps,
  Paper,
  MenuItem,
  FormControlLabel,
  Checkbox,
  Accordion,
  AccordionSummary,
  AccordionDetails,
} from '@material-ui/core';
import Draggable from 'react-draggable';
import { Close, ExpandMore } from '@material-ui/icons';

import { useTranslation } from 'react-i18next';
import { useDispatch, useSelector } from 'hooks';
import { useSnackbar } from 'notistack';

import { createProduct, updateProduct } from 'store/products/actions';
import { getUnitsOfMeasure } from 'interfaces/models/UnitOfMeasure';
import { getOrganizationTypes } from 'interfaces/models/OrganizationType';
import ProductDto from 'interfaces/dto/ProductDto';
import OrganizationDto from 'interfaces/dto/OrganizationDto';

const useDialogStyles = makeStyles(theme => ({
  paper: {
    borderRadius: 12,
  },
  header: {
    height: 48,
  },
  headerIcon: {
    marginRight: 4,
    color: theme.palette.error.main,
  },
  grid: {
    width: '100%',
    height: '100%',
    padding: theme.spacing(1),
  },
  headerText: {
    fontFamily: 'Google Sans',
    fontSize: 20,
    fontWeight: 500,
  },
  hasManufacturer: {
    paddingTop: theme.spacing(2),
    paddingRight: theme.spacing(2),
  },
  backdrop: {
    zIndex: 100,
  },
}));

const Transition = React.forwardRef(function Transition(
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  props: TransitionProps & { children?: React.ReactElement<any, any> },
  ref: React.Ref<unknown>
) {
  return <Fade ref={ref} {...props} />;
});

interface Props {
  productId?: number;
  isOpen: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
}

const DraggablePaper = (props: PaperProps) => {
  return (
    <Draggable
      handle="#add-or-edit-dialog-title"
      cancel={'[class*="MuiDialogContent-root"]'}
    >
      <Paper {...props} />
    </Draggable>
  );
};

const isBlank = (str: string) => {
  return (!str || /^\s*$/.test(str));
};

const nullOrUndefined = value => value === null || value === undefined;

const AddOrEdit: React.FC<Props> = ({ productId, isOpen, setOpen }) => {
  const classes = useDialogStyles();

  const type = !productId ? 'Add' : 'Edit';
  const isEdit = !!productId;

  const { enqueueSnackbar } = useSnackbar();
  const { t } = useTranslation();

  const dispatch = useDispatch();
  const createIsFetching = useSelector(state => state.products.create.fetching);
  const updateIsFetching = useSelector(state => state.products.update.fetching);
  const products = useSelector(state => state.products.products);

  const getInputState = (productId) => {
    const product = products.filter(p => p.id === productId)[0];
    return {
      name: product?.name,
      x: product?.x || 0,
      y: product?.y || 0,
      price: product?.price || 0,
      partNumber: product?.partNumber || '',
      unitOfMeasure: product?.unitOfMeasure || 'KILOGRAMS',
      hasManufacturer: !!product?.manufacturer?.id || false,
      manufacturerName: product?.manufacturer?.name || '',
      manufacturerEmployeesCount: product?.manufacturer?.employeesCount || 0,
      manufacturerType: product?.manufacturer?.type || 'COMMERCIAL',
      manufacturerStreet: product?.manufacturer?.street || '',
      manufacturerZipCode: product?.manufacturer?.zipCode || '',
    };
  };

  const buildRequest = (): ProductDto => {
    let manufacturer: OrganizationDto = null;
    const partNumberEmpty = nullOrUndefined(partNumber) || partNumber === '';
    const pn = partNumberEmpty ? null : partNumber;
    if (hasManufacturer) {
      manufacturer = {
        name: manufacturerName,
        employeesCount: manufacturerEmployeesCount.toString(),
        type: manufacturerType,
        street: manufacturerStreet,
        zipCode: isManufacturerZipCodeError() ? null : manufacturerZipCode,
      };
    }
    return { name, x, y, price, partNumber: pn, unitOfMeasure, manufacturer };
  };

  const [inputs, setInputs] = useState(getInputState(productId));

  const {
    name,
    x,
    y,
    price,
    partNumber,
    unitOfMeasure,
    hasManufacturer,
    manufacturerName,
    manufacturerEmployeesCount,
    manufacturerType,
    manufacturerStreet,
    manufacturerZipCode,
  } = inputs;

  const isNameError = () => nullOrUndefined(name) || isBlank(name);
  const nameErrorMessage = () => t('pages.Home.modals.form.errors.name');

  const isXError = () => nullOrUndefined(x);
  const xErrorMessage = () => t('pages.Home.modals.form.errors.x');

  const isYError = () => nullOrUndefined(y);
  const yErrorMessage = () => t('pages.Home.modals.form.errors.y');

  const isPriceError = () => nullOrUndefined(price) || price <= 0;
  const priceErrorMessage = () => t('pages.Home.modals.form.errors.price');

  const isPartNumberError = () => nullOrUndefined(partNumber) || isBlank(partNumber);
  const partNumberErrorMessage = () => t('pages.Home.modals.form.errors.partNumber');

  const isManufacturerNameError = () =>
    nullOrUndefined(manufacturerName) || isBlank(manufacturerName);
  const manufacturerNameErrorMessage = () => t('pages.Home.modals.form.errors.manufacturerName');

  const isManufacturerEmployeesCountError = () =>
    nullOrUndefined(manufacturerEmployeesCount) || manufacturerEmployeesCount <= 0;
  const manufacturerEmployeesCountErrorMessage = () =>
    t('pages.Home.modals.form.errors.manufacturerEmployeesCount');

  const isManufacturerTypeError = () => nullOrUndefined(manufacturerType);
  const manufacturerTypeErrorMessage = () => t('pages.Home.modals.form.errors.manufacturerType');

  const isManufacturerStreetError = () =>
    nullOrUndefined(manufacturerStreet) || isBlank(manufacturerStreet);
  const manufacturerStreetErrorMessage = () => t('pages.Home.modals.form.errors.manufacturerStreet');

  const isManufacturerZipCodeError = () =>
    !nullOrUndefined(manufacturerZipCode) && manufacturerZipCode.length < 6;
  const manufacturerZipCodeErrorMessage = () => t('pages.Home.modals.form.errors.manufacturerZipCode');

  const shouldDisableButton = createIsFetching
    || isNameError()
    || isXError()
    || isYError()
    || isPriceError()
    || isPartNumberError()
    || hasManufacturer && (
      isManufacturerNameError()
      || isManufacturerEmployeesCountError()
      || isManufacturerTypeError()
      || isManufacturerStreetError()
      || isManufacturerZipCodeError()
    ) || updateIsFetching;

  const handleInputsChange = e => {
    const { name, value } = e.target;
    setInputs(inputsBefore => ({ ...inputsBefore, [name]: value }));
  };

  const handleHasManufacturerChange = e => {
    setInputs(
      inputsBefore => ({ ...inputsBefore, hasManufacturer: e.target.checked })
    );
  };

  const handleCancel = () => setOpen(false);
  const handleOk = () => {
    dispatch(
      isEdit ? updateProduct(productId, buildRequest()) : createProduct(buildRequest())
    ).then(() => {
      enqueueSnackbar(t(`pages.Home.modals.${type}.success`), {
        variant: 'success',
        autoHideDuration: 2000,
      });
    }).catch(error => {
      enqueueSnackbar(t(`pages.Home.modals.${type}.errors.${error || 'ERROR'}`), {
        variant: 'error',
        autoHideDuration: 4000,
      });
    }).finally(() => setOpen(false));
  };

  const handleSubmit = async e => {
    e.preventDefault();
    handleOk();
  };

  return (
    <>
      <Dialog
        disableBackdropClick
        disableEscapeKeyDown
        maxWidth="sm"
        fullWidth={true}
        onClose={handleCancel}
        open={isOpen}
        scroll="body"
        aria-labelledby="add-or-edit-dialog-title"
        TransitionComponent={Transition}
        PaperComponent={DraggablePaper}
        classes={{ paper: classes.paper }}
      >
        <DialogTitle
          id="add-or-edit-dialog-title"
          style={{ cursor: 'move' }}
          className={classes.header}
        >
          <Grid container direction="row" justifyContent="space-between" alignItems="center">
            <Typography className={classes.headerText}>
              {t(`pages.Home.modals.${type}.title`)}
            </Typography>
            <IconButton className={classes.headerIcon} onClick={handleCancel}>
              <Close width={24} height={24} />
            </IconButton>
          </Grid>
        </DialogTitle>

        <DialogContent dividers>
          <DialogContentText id="add-or-edit-dialog-description">
            {t(`pages.Home.modals.${type}.description`)}
          </DialogContentText>

          <form onSubmit={handleSubmit}>
            <Grid container direction="column" className={classes.grid}>
              <Grid item>
                <TextField
                  name="name"
                  label={t('pages.Home.modals.form.name')}
                  error={isNameError()}
                  helperText={isNameError() ? nameErrorMessage() : ' '}
                  value={name}
                  autoFocus
                  multiline

                  fullWidth
                  onChange={handleInputsChange}
                  margin="dense"
                  size="small"
                  variant="standard"
                />
              </Grid>
              <Grid item>
                <Grid container spacing={2}>
                  <Grid item>
                    <TextField
                      name="x"
                      label={t('pages.Home.modals.form.x')}
                      error={isXError()}
                      helperText={isXError() ? xErrorMessage() : ' '}
                      type="number"
                      value={x}
                      InputLabelProps={{
                        shrink: true,
                      }}

                      onChange={handleInputsChange}
                      margin="dense"
                      size="small"
                      variant="standard"
                    />
                  </Grid>
                  <Grid item>
                    <TextField
                      name="y"
                      label={t('pages.Home.modals.form.y')}
                      error={isYError()}
                      helperText={isYError() ? yErrorMessage() : ' '}
                      type="number"
                      value={y}
                      InputLabelProps={{
                        shrink: true,
                      }}

                      onChange={handleInputsChange}
                      margin="dense"
                      size="small"
                      variant="standard"
                    />
                  </Grid>
                </Grid>
              </Grid>
              <Grid item>
                <TextField
                  name="price"
                  label={t('pages.Home.modals.form.price')}
                  error={isPriceError()}
                  helperText={isPriceError() ? priceErrorMessage() : ' '}
                  type="number"
                  value={price}
                  InputLabelProps={{
                    shrink: true,
                  }}

                  onChange={handleInputsChange}
                  margin="dense"
                  size="small"
                  variant="standard"
                />
              </Grid>
              <Grid item>
                <TextField
                  name="partNumber"
                  label={t('pages.Home.modals.form.partNumber')}
                  error={isPartNumberError()}
                  helperText={
                    isPartNumberError() ? partNumberErrorMessage() : ' '
                  }
                  value={partNumber}

                  fullWidth
                  onChange={handleInputsChange}
                  margin="dense"
                  size="small"
                  variant="standard"
                />
              </Grid>
              <Grid item>
                <TextField
                  name="unitOfMeasure"
                  label={t('pages.Home.modals.form.unitOfMeasure')}
                  helperText={' '}
                  value={unitOfMeasure}
                  select

                  fullWidth
                  onChange={handleInputsChange}
                  margin="dense"
                  size="small"
                  variant="standard"
                >
                  {Object.keys(getUnitsOfMeasure()).map(key => (
                    <MenuItem key={key} value={key}>
                      {getUnitsOfMeasure()[key]}
                    </MenuItem>
                  ))}
                </TextField>
              </Grid>
              <Grid item>
                <FormControlLabel className={classes.hasManufacturer}
                  control={
                    <Checkbox
                      color="primary"
                      checked={hasManufacturer}
                      onChange={handleHasManufacturerChange}
                    />
                  }
                  label={t('pages.Home.modals.form.hasManufacturer')}
                  labelPlacement="end"
                />
              </Grid>
              {hasManufacturer && (
                <Accordion expanded={hasManufacturer} disabled={!hasManufacturer}>
                  <AccordionSummary
                    expandIcon={<ExpandMore />}
                  >
                    {t('pages.Home.modals.form.inputManufacturer')}
                  </AccordionSummary>
                  <AccordionDetails>
                    <Grid container direction="column" className={classes.grid}>
                      <Grid item>
                        <TextField
                          name="manufacturerName"
                          label={t('pages.Home.modals.form.manufacturerName')}
                          error={isManufacturerNameError()}
                          helperText={
                            isManufacturerNameError() ? manufacturerNameErrorMessage() : ' '
                          }
                          value={manufacturerName}
                          multiline

                          fullWidth
                          onChange={handleInputsChange}
                          margin="dense"
                          size="small"
                          variant="standard"
                        />
                      </Grid>
                      <Grid item>
                        <Grid container spacing={2}>
                          <Grid item xs={6}>
                            <TextField
                              name="manufacturerEmployeesCount"
                              label={t('pages.Home.modals.form.manufacturerEmployeesCount')}
                              error={isManufacturerEmployeesCountError()}
                              helperText={
                                isManufacturerEmployeesCountError()
                                  ? manufacturerEmployeesCountErrorMessage()
                                  : ' '
                              }
                              type="number"
                              value={manufacturerEmployeesCount}
                              InputLabelProps={{
                                shrink: true,
                              }}

                              fullWidth
                              onChange={handleInputsChange}
                              margin="dense"
                              size="small"
                              variant="standard"
                            />
                          </Grid>
                          <Grid item xs={6}>
                            <TextField
                              name="manufacturerType"
                              label={t('pages.Home.modals.form.manufacturerType')}
                              error={isManufacturerTypeError()}
                              helperText={
                                isManufacturerTypeError() ? manufacturerTypeErrorMessage() : ' '
                              }
                              value={manufacturerType}
                              select

                              fullWidth
                              onChange={handleInputsChange}
                              margin="dense"
                              size="small"
                              variant="standard"
                            >
                              {Object.keys(getOrganizationTypes()).map(key => (
                                <MenuItem key={key} value={key}>
                                  {getOrganizationTypes()[key]}
                                </MenuItem>
                              ))}
                            </TextField>
                          </Grid>
                        </Grid>
                      </Grid>
                      <Grid item>
                        <TextField
                          name="manufacturerStreet"
                          label={t('pages.Home.modals.form.manufacturerStreet')}
                          error={isManufacturerStreetError()}
                          helperText={
                            isManufacturerStreetError() ? manufacturerStreetErrorMessage() : ' '
                          }
                          value={manufacturerStreet}
                          multiline

                          fullWidth
                          onChange={handleInputsChange}
                          margin="dense"
                          size="small"
                          variant="standard"
                        />
                      </Grid>
                      <Grid item>
                        <TextField
                          name="manufacturerZipCode"
                          label={t('pages.Home.modals.form.manufacturerZipCode')}
                          error={isManufacturerZipCodeError()}
                          helperText={
                            isManufacturerZipCodeError() ? manufacturerZipCodeErrorMessage() : ' '
                          }
                          value={manufacturerZipCode}
                          multiline

                          fullWidth
                          onChange={handleInputsChange}
                          margin="dense"
                          size="small"
                          variant="standard"
                        />
                      </Grid>
                    </Grid>
                  </AccordionDetails>
                </Accordion>
              )}
            </Grid>
          </form>
        </DialogContent>

        <DialogActions>
          <Button onClick={handleCancel} color="primary">
            {t(`pages.Home.modals.${type}.cancel`)}
          </Button>
          <Button onClick={handleOk} type="submit" disabled={shouldDisableButton} color="primary">
            {t(`pages.Home.modals.${type}.ok`)}
          </Button>
        </DialogActions>
      </Dialog>
      {(createIsFetching || updateIsFetching) && <Backdrop
        className={classes.backdrop}
        open={createIsFetching || updateIsFetching}
      >
        <CircularProgress color="primary" />
      </Backdrop>
      }
    </>
  );
};

export default React.memo(AddOrEdit);
