import api from 'services/api/api';
import { AxiosResponse } from 'axios';

import Product from 'interfaces/models/Product';
import ProductDto from 'interfaces/dto/ProductDto';
import DeleteResponse from 'interfaces/dto/DeleteResponse';

export default class ProductService {
  static async getAll(): Promise<AxiosResponse<Product[]>> {
    return api.get<Product[]>('/products');
  }

  static async get(id: number): Promise<AxiosResponse<Product>> {
    return api.get<Product>(`/products/${id}`);
  }

  static async create(product: ProductDto): Promise<AxiosResponse<Product>> {
    return api.post<Product>('/products', product);
  }

  static async update(id: number, product: ProductDto): Promise<AxiosResponse<Product>> {
    return api.put<Product>(`/products/${id}`, product);
  }

  static async delete(id: number): Promise<AxiosResponse<DeleteResponse>> {
    return api.delete<DeleteResponse>(`/products/${id}`);
  }

  static async clear(): Promise<AxiosResponse<DeleteResponse>> {
    return api.delete<DeleteResponse>('/products');
  }
}
