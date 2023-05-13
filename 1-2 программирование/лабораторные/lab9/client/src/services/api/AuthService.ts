import api from 'services/api/api';

import { AxiosResponse } from 'axios';
import Credentials from 'interfaces/dto/Credentials';
import AuthResponse from 'interfaces/dto/AuthResponse';

export default class AuthService {
  /**
   * Get current user
   * @returns {Promise<AxiosResponse<AuthResponse>>} User's data and token
   * @throws 401 - Unauthorized
   * @throws 422 - Bad token user ID
   */
  static async me(): Promise<AxiosResponse<AuthResponse>> {
    return api.get<AuthResponse>('/auth');
  }

  /**
   * Login user
   * @param {Credentials} credentials Username & Password
   * @returns {Promise<AxiosResponse<AuthResponse>>} User's data and token
   * @throws 400 - Invalid credentials
   * @throws 403 - Cannot log in
   */
  static async login(credentials: Credentials): Promise<AxiosResponse<AuthResponse>> {
    return api.post<AuthResponse>('/auth/login', credentials);
  }

  /**
   * Create user
   * @param {Credentials} credentials Username & Password
   * @returns {Promise<AxiosResponse<AuthResponse>>} User's data and token
   * @throws 400 - Invalid credentials
   * @throws 409 - User.ts with this username already exists
   */
  static async register(credentials: Credentials): Promise<AxiosResponse<AuthResponse>> {
    return api.post<AuthResponse>('/auth/register', credentials);
  }
}
