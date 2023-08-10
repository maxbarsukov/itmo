import api from 'services/api/api';

import { AxiosResponse } from 'axios';
import InfoResponse from 'interfaces/dto/InfoResponse';

export default class InfoService {
  /**
   * Get info about server
   * @returns {Promise<AxiosResponse<InfoResponse>>} Information about server
   * @throws 500 - Internal error
   */
  static async info(): Promise<AxiosResponse<InfoResponse>> {
    return api.get<InfoResponse>('/info');
  }
}
