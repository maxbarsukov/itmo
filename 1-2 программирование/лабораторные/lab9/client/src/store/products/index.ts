import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';
import Product from 'interfaces/models/Product';
import Storage from 'utils/Storage';
import { LAST_UPDATED_KEY, UPDATED_BY_KEY, USER_KEY } from 'config/constants';
import i18n from 'config/i18n';
import {
  CreateEvent,
  UpdateEvent,
  DeleteEvent,
  ClearEvent,
} from 'interfaces/events';

interface PendingState {
  fetching: boolean;
  fetched: boolean;
  error?: string;
}

export interface ProductsState {
  products: Product[];
  productsCount: number;
  lastUpdated?: number;
  updatedBy?: string;
  getAll: PendingState;
  create: PendingState;
  update: PendingState;
  delete: PendingState;
  clear: PendingState;
}

const getCurrentName = () => {
  const t = i18n.t.bind(i18n);
  return Storage.get(USER_KEY)?.name || t('pages.app.other.nobodyUpdated');
};

const t = i18n.t.bind(i18n);
const state = {
  products: [],
  productsCount: 0,
  lastUpdated: Storage.get(LAST_UPDATED_KEY) || Date.now(),
  updatedBy: Storage.get(UPDATED_BY_KEY) || t('pages.app.other.nobodyUpdated'),
};

['getAll', 'create', 'update', 'delete', 'clear'].forEach(command => {
  state[command] = {
    fetching: false,
    fetched: false,
    error: null,
  };
});

const initialState: ProductsState = state as ProductsState;

const loading = (state) => {
  state.fetching = true;
  state.fetched = false;
  state.error = null;
};

const success = (state) => {
  state.fetching = false;
  state.fetched = true;
  state.error = null;
};

const fail = (state, error) => {
  state.error = error;
  state.fetching = false;
  state.fetched = false;
};

const updated = (state, name = getCurrentName()) => {
  state.lastUpdated = Date.now();
  Storage.set(LAST_UPDATED_KEY, state.lastUpdated);
  state.updatedBy = name;
  Storage.set(UPDATED_BY_KEY, name);
};

export const productsSlice = createSlice({
  name: 'products',
  initialState,
  reducers: {
    createEvent: (state, action: PayloadAction<CreateEvent>) => {
      if (!state.products.map(p => p.id).includes(action.payload.createdProduct.id)) {
        state.products.push(action.payload.createdProduct);
        state.productsCount += 1;
      }
      updated(state, action.payload.creator.name);
    },
    updateEvent: (state, action: PayloadAction<UpdateEvent>) => {
      if (state.products.map(p => p.id).includes(action.payload.updatedProduct.id)) {
        state.products = state.products.map(product =>
          product.id === action.payload.updatedProduct.id ? action.payload.updatedProduct : product
        );
      } else {
        state.products.push(action.payload.updatedProduct);
        state.productsCount += 1;
      }
      updated(state, action.payload.creator.name);
    },
    deleteEvent: (state, action: PayloadAction<DeleteEvent>) => {
      state.products = state.products.filter(product =>
        product.id !== action.payload.deletedId
      );
      state.productsCount = state.products.length;
      updated(state, action.payload.creator.name);
    },
    clearEvent: (state, action: PayloadAction<ClearEvent>) => {
      state.products = state.products.filter(product =>
        !action.payload.deletedIds.includes(product.id)
      );
      state.productsCount = state.products.length;
      updated(state, action.payload.creator.name);
    },

    getAllProductsLoading: state => {
      loading(state.getAll);
    },
    getAllProductsSuccess: (state, action: PayloadAction<Product[]>) => {
      const products = action.payload;
      state.products = products;
      state.productsCount = products.length;
      success(state.getAll);
    },
    getAllProductsFail: (state, action: PayloadAction<string>) => {
      fail(state.getAll, action.payload);
    },

    createProductLoading: state => {
      loading(state.create);
    },
    createProductSuccess: (state, action: PayloadAction<Product>) => {
      state.products.push(action.payload);
      state.productsCount += 1;
      updated(state);
      success(state.create);
    },
    createProductFail: (state, action: PayloadAction<string>) => {
      fail(state.create, action.payload);
    },

    updateProductLoading: state => {
      loading(state.update);
    },
    updateProductSuccess: (state, action: PayloadAction<{ product: Product; id: number }>) => {
      state.products = state.products.map(product =>
        product.id === action.payload.id ? action.payload.product : product
      );
      updated(state);
      success(state.update);
    },
    updateProductFail: (state, action: PayloadAction<string>) => {
      fail(state.update, action.payload);
    },

    deleteProductLoading: state => {
      loading(state.delete);
    },
    deleteProductSuccess: (state, action: PayloadAction<number>) => {
      state.products = state.products.filter(product =>
        product.id !== action.payload
      );
      state.productsCount = state.products.length;
      updated(state);
      success(state.delete);
    },
    deleteProductFail: (state, action: PayloadAction<string>) => {
      fail(state.delete, action.payload);
    },

    clearProductsLoading: state => {
      loading(state.clear);
    },
    clearProductsSuccess: (state, action: PayloadAction<number>) => {
      const creatorId = action.payload;
      state.products = state.products.filter(product =>
        product.creator.id !== creatorId
      );
      state.productsCount = state.products.length;
      updated(state);
      success(state.clear);
    },
    clearProductsFail: (state, action: PayloadAction<string>) => {
      fail(state.clear, action.payload);
    },
  },
});

export const {
  createEvent,
  updateEvent,
  deleteEvent,
  clearEvent,
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
} = productsSlice.actions;
export default productsSlice.reducer;
