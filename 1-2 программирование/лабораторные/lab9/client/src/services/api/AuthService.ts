import api from 'services/api/api';

export default class AuthService {
  /**
   * Login for access_token
   * @param {string} username Username
   * @param {string} password Password
   * @returns {Promise<AxiosResponse<any>>} access_token
   */
  static async token(username, password) {
    const data = {
      username,
      password,
    };
    const encodedData = Object.keys(data)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(data[key])}`)
      .join('&');
    return api.post('/token', encodedData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
  }

  /**
   * Create user
   * @param {string} username
   * @param {string} email
   * @param {string} password
   * @returns {Promise<AxiosResponse<any>>} User's data
   * @throws 409 - User with this email/username already exists
   * @throws 422 - Validation Error
   */
  static async register(username, email, password) {
    return api.post('/users', { username, email, password });
  }
}
