import User from 'interfaces/User';
import Response from 'interfaces/dto/Response';

type AuthResponse = {
  token: string;
  user: User;
};

type response = Response<AuthResponse>;
export default response;
