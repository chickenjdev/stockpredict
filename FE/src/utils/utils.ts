import { parse } from 'querystring';

/* eslint no-useless-escape:0 import/prefer-default-export:0 */
const reg = /(((^https?:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+(?::\d+)?|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)$/;

export const isUrl = (path: string): boolean => reg.test(path);

export const isAntDesignPro = (): boolean => {
  if (ANT_DESIGN_PRO_ONLY_DO_NOT_USE_IN_YOUR_PRODUCTION === 'site') {
    return true;
  }
  return window.location.hostname === 'preview.pro.ant.design';
};

export const isAntDesignProOrDev = (): boolean => {
  const { NODE_ENV } = process.env;
  if (NODE_ENV === 'development') {
    return true;
  }
  return isAntDesignPro();
};

export const isProduction = (): boolean => {
  if (REACT_APP_ENV === 'prod') {
    return true;
  }
  return false;
};

export const getPageQuery = () => parse(window.location.href.split('?')[1]);

export function setDataLocal(data: string | string[], key: string): void {
  const proAuthority = typeof data === 'string' ? [data] : data;
  localStorage.setItem(key, JSON.stringify(proAuthority));
}

export function clearAll(): void {
  localStorage.clear();
}

export function getDataLocal(key: string): string | string[] {
  const authorityString =
    typeof key === 'string' && localStorage ? localStorage.getItem(key) : null;
  let authority;
  try {
    if (authorityString) {
      authority = JSON.parse(authorityString);
    }
  } catch (e) {
    authority = authorityString;
  }
  if (typeof authority === 'string') {
    return [authority];
  }
  return authority;
}
