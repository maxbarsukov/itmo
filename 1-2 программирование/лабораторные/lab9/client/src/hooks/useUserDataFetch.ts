import { useEffect } from 'react';
import { useDispatch } from 'hooks';

import { loadCurrentUser } from 'store/auth/actions';
import Storage from 'utils/Storage';
import { TOKEN_KEY } from 'config/constants';

const useUserDataFetch = () => {
  const isOnline = navigator.onLine;

  if (!isOnline) return;

  const dispatch = useDispatch();

  useEffect(() => {
    const token = Storage.get(TOKEN_KEY);
    if (token) {
      dispatch(loadCurrentUser());
    }
  }, []);
};

export default useUserDataFetch;
