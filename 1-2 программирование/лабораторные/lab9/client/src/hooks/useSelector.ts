import { useSelector as useReduxSelector, TypedUseSelectorHook } from 'react-redux';
import { RootState } from 'store';

const useSelector: TypedUseSelectorHook<RootState> = useReduxSelector;

export default useSelector;
