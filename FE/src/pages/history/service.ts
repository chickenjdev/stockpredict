import { API_TELCO } from "@/utils/constants";
import MServiceRequest, { environment } from "@/utils/MServiceRequest";

export async function getHistory(params: any) {
  return MServiceRequest(API_TELCO.GET_HISTORY, {
    method: 'GET',
    params,
    mode: 'cors',
    prefix: environment(),
  });
}

