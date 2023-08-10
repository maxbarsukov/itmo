import {
  getAllProductsLoading,
  getAllProductsSuccess,
  getAllProductsFail,
  createProductLoading,
  createProductSuccess,
  createProductFail,
  updateProductLoading,
  updateProductSuccess,
  updateProductFail,
  deleteProductLoading,
  deleteProductSuccess,
  deleteProductFail,
  clearProductsLoading,
  clearProductsSuccess,
  clearProductsFail,
} from 'store/products';
import { addRequest } from 'store/requests';

import ProductService from 'services/api/ProductService';

import ProductDto from 'interfaces/dto/ProductDto';

const getErrorMessage = error => error?.response?.data?.message || 'ERROR';

export const getAllProducts = () => dispatch => {
  dispatch(getAllProductsLoading());

  return ProductService.getAll().then(
    response => {
      const { data } = response;
      dispatch(getAllProductsSuccess(data));
      return Promise.resolve();
    },
    error => {
      dispatch(getAllProductsFail(getErrorMessage(error)));
      return Promise.reject();
    }
  );
};

export const createProduct = (product: ProductDto) => dispatch => {
  dispatch(createProductLoading());

  return ProductService.create(product).then(
    response => {
      const { data, headers } = response;
      dispatch(addRequest(headers['x-response-uuid']));
      dispatch(createProductSuccess(data));
      return Promise.resolve();
    },
    error => {
      dispatch(createProductFail(getErrorMessage(error)));
      return Promise.reject();
    }
  );
};

export const updateProduct = (id: number, product: ProductDto) => dispatch => {
  dispatch(updateProductLoading());

  return ProductService.update(id, product).then(
    response => {
      const { data, headers } = response;
      dispatch(addRequest(headers['x-response-uuid']));
      dispatch(updateProductSuccess({ product: data, id }));
      return Promise.resolve();
    },
    error => {
      dispatch(updateProductFail(getErrorMessage(error)));
      return Promise.reject();
    }
  );
};

export const deleteProduct = (id: number) => dispatch => {
  dispatch(deleteProductLoading());

  return ProductService.delete(id).then(
    response => {
      const { headers } = response;
      dispatch(addRequest(headers['x-response-uuid']));
      dispatch(deleteProductSuccess(id));
      return Promise.resolve();
    },
    error => {
      dispatch(deleteProductFail(getErrorMessage(error)));
      return Promise.reject(getErrorMessage(error));
    }
  );
};

export const clearProducts = (currentUserId: number) => dispatch => {
  dispatch(clearProductsLoading());

  return ProductService.clear().then(
    response => {
      const { headers } = response;
      dispatch(addRequest(headers['x-response-uuid']));
      dispatch(clearProductsSuccess(currentUserId));
      return Promise.resolve();
    },
    error => {
      dispatch(clearProductsFail(getErrorMessage(error)));
      return Promise.reject();
    }
  );
};
