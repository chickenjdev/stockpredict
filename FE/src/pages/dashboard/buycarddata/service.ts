import { API_TELCO } from '@/utils/constants';
import MServiceRequest, { environment } from '@/utils/MServiceRequest';

export async function getBalancing(params: any) {
  return MServiceRequest(API_TELCO.GET_BALANCING, {
    method: 'GET',
    params,
    mode: 'cors',
    prefix: environment(),
  });
}
