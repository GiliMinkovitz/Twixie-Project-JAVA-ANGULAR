import { HttpInterceptorFn } from '@angular/common/http';

export const credentialsInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token'); // או מאיפה שאת שומרת את הטוקן
  const clonedRequest = req.clone({
    withCredentials: true,
    setHeaders: token ? { Authorization: `Bearer ${token}` } : {},
  });
  return next(clonedRequest);
};
