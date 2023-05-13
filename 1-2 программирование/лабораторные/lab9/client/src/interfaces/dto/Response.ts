export type ErrorResponse = {
  message: string;
  description?: string;
};

type Response<T> = T | ErrorResponse;

export default Response;
