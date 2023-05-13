import React from 'react';
import '@testing-library/jest-dom';
import { configure } from 'enzyme';
import Adapter from '@cfaester/enzyme-adapter-react-18';

React.useLayoutEffect = React.useEffect;
configure({ adapter: new Adapter() });
