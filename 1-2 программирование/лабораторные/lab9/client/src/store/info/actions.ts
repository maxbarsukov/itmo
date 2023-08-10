import {
  getInfoLoading,
  getInfoSuccess,
  getInfoFail,
} from 'store/info';
import InfoService from 'services/api/InfoService';

const getErrorMessage = error => error?.response?.data?.message || 'ERROR';

export const getInfo = () => dispatch => {
  dispatch(getInfoLoading());

  return InfoService.info().then(
    response => {
      const { data } = response;
      dispatch(getInfoSuccess(data));
      return Promise.resolve();
    },
    error => {
      dispatch(getInfoFail(getErrorMessage(error)));
      return Promise.reject();
    }
  );
};
