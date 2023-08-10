import User from 'interfaces/models/User';

type AuthResponse = {
  token: string;
  user: User;
};

export default AuthResponse;
