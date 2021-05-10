import type { RequestOptionsInit } from 'umi-request';
import { extend } from 'umi-request';
import { notification } from 'antd';
import { BASE_URL_TELCO, BASE_URL_TELCO_PRO } from './constants';
import { isProduction } from './utils';

const codeMessage = {
  200: 'The server successfully returned the requested data.',
  201: 'New or modified data is successful.',
  202: 'A request has entered the background queue (asynchronous task).',
  204: 'The data was deleted successfully.',
  400: 'There was an error in the request sent, and the server did not create or modify data.',
  401: 'The user does not have permission (the token, username, password is wrong).',
  403: 'The user is authorized, but access is forbidden.',
  404: 'The request sent was for a record that did not exist, and the server did not operate.',
  406: 'The requested format is not available.',
  410: 'The requested resource is permanently deleted and will no longer be available.',
  422: 'When creating an object, a validation error occurred.',
  500: 'An error occurred in the server, please check the server.',
  502: 'Gateway error.',
  503: 'The service is unavailable, and the server is temporarily overloaded or maintained.',
  504: 'The gateway timed out.',
};

/**
 * Exception handler
 */
const errorHandler = (error: { response: Response }): Response => {
  const { response } = error;
  if (response && response.status) {
    const errorText = codeMessage[response.status] || response.statusText;
    const { status, url } = response;

    notification.error({
      message: `Request error ${status}: ${url}`,
      description: errorText,
    });
  } else if (!response) {
    notification.error({
      description: 'Your network is abnormal and cannot connect to the server',
      message: 'network anomaly',
    });
  }
  return response;
};

/**
 * Configure the default parameters for request
 */
export const environment = (): string => {
  let env = BASE_URL_TELCO;
  if (isProduction()) {
    env = BASE_URL_TELCO_PRO;
  }
  return env;
};

/**
 * Configure the default parameters for request
 */
const request = extend({
  prefix: BASE_URL_TELCO,
  errorHandler, // Default error handling
  // mode: "no-cors",
  timeout: 50000,
  headers: {
    // Authorization:
    //   'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjoiMDkxNDExMTExMSIsIk5BTUUiOiJBbmggRHVjIDEiLCJpbWVpIjoiNmU5MzYxZDgtNzY1My00ZDM0LWI4ZTQtMjQzNTlkZjA3NzIxIiwiYWdlbnRfaWQiOjM3MDA4NDI3LCJCQU5LX0NPREUiOiIzMDEiLCJCQU5LX05BTUUiOiJWaWV0Y29tYmFuayIsIlZBTElEX1dBTExFVF9DT05GSVJNIjoxLCJNQVBfU0FDT01fQ0FSRCI6MCwiSURFTlRJRlkiOiJDT05GSVJNIiwiREVWSUNFX09TIjoiQU5EUk9JRCIsInNlc3Npb25LZXkiOiJET0lzRFo4L2ZQeDJuc001NzRtRXE0TGxXL0J5SUhSWWt1Q1Z0NFFYdTdaajJaV3kxNHdBdWc9PSIsInBpbiI6Ii9wNVR3a3FnalFZPSIsInBpbl9lbmNyeXB0Ijp0cnVlLCJ2ZXJpZnlJbmZvIjoiMTIzNCIsImJhbmtWZXJpZnlOYW1lIjoiUEhBVCBEQVQiLCJiYW5rVmVyaWZ5UGVyc29uYWxJZCI6IjEyMzQ1NiIsImxldmVsIjowLCJBUFBfVkVSIjozMDAwMiwiaWF0IjoxNjEwODgxMzU0fQ.Jm-rVFZIFCacoXQ9kppwNAAHGe8DZNT3BDMeSLDqem5I881YN1iM9nRQC-avnayy2xQZTln1GzBnPBbAjl8Dqq1sDRub70jGAhy4HDKlTBLNNyznrztNrxdQnT4zhSGW9YIurGOvvYhTqi288P25SuP0Os2-h-NubtOkoo8uQxp7t1baSC2ybTElo9OybI-otNmCzr2RRbBg5rQf_FGheeI11E5l2A5gVwRXaq7rKZsYZN8KxVk_hSkB0Na4H9q7ZV_BOw8ufEgvLBSKdXcngTfPIutPKt2rwLopECPns4fUDm-MaDqrz7Q2JHy3VqeTHRpPLz7x-5_KwPKlWdq-Ww',
  },
});

const headers = {
  // Authorization:
  //   'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjoiMDkxNDExMTExMSIsIk5BTUUiOiJBbmggRHVjIDEiLCJpbWVpIjoiNmU5MzYxZDgtNzY1My00ZDM0LWI4ZTQtMjQzNTlkZjA3NzIxIiwiYWdlbnRfaWQiOjM3MDA4NDI3LCJCQU5LX0NPREUiOiIzMDEiLCJCQU5LX05BTUUiOiJWaWV0Y29tYmFuayIsIlZBTElEX1dBTExFVF9DT05GSVJNIjoxLCJNQVBfU0FDT01fQ0FSRCI6MCwiSURFTlRJRlkiOiJDT05GSVJNIiwiREVWSUNFX09TIjoiQU5EUk9JRCIsInNlc3Npb25LZXkiOiJET0lzRFo4L2ZQeDJuc001NzRtRXE0TGxXL0J5SUhSWWt1Q1Z0NFFYdTdaajJaV3kxNHdBdWc9PSIsInBpbiI6Ii9wNVR3a3FnalFZPSIsInBpbl9lbmNyeXB0Ijp0cnVlLCJ2ZXJpZnlJbmZvIjoiMTIzNCIsImJhbmtWZXJpZnlOYW1lIjoiUEhBVCBEQVQiLCJiYW5rVmVyaWZ5UGVyc29uYWxJZCI6IjEyMzQ1NiIsImxldmVsIjowLCJBUFBfVkVSIjozMDAwMiwiaWF0IjoxNjEwODgxMzU0fQ.Jm-rVFZIFCacoXQ9kppwNAAHGe8DZNT3BDMeSLDqem5I881YN1iM9nRQC-avnayy2xQZTln1GzBnPBbAjl8Dqq1sDRub70jGAhy4HDKlTBLNNyznrztNrxdQnT4zhSGW9YIurGOvvYhTqi288P25SuP0Os2-h-NubtOkoo8uQxp7t1baSC2ybTElo9OybI-otNmCzr2RRbBg5rQf_FGheeI11E5l2A5gVwRXaq7rKZsYZN8KxVk_hSkB0Na4H9q7ZV_BOw8ufEgvLBSKdXcngTfPIutPKt2rwLopECPns4fUDm-MaDqrz7Q2JHy3VqeTHRpPLz7x-5_KwPKlWdq-Ww',
};
request.interceptors.request.use(
  (url: string, options: RequestOptionsInit) => {
    return {
      url,
      options: { ...options, headers, interceptors: false },
    };
  },
  { global: true },
);
export default request;
