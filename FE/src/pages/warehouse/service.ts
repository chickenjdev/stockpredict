import { API_TELCO } from "@/utils/constants";
import MServiceRequest, { environment } from "@/utils/MServiceRequest";

export async function getWarehouseList(params: any) {
  return MServiceRequest(API_TELCO.GET_WARE_HOUSELIST, {
    method: 'GET',
    params,
    mode: 'cors',
    prefix: environment(),
  });
}

export async function updateConfig(params: any) {
  return new Promise((resolve, reject) => {
    fetch(environment() + API_TELCO.POST_UPDATE_CONFIG, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
      body: JSON.stringify(params),
    })
      .then((response) => response.json())
      .then((responseJson) => {
        resolve(responseJson);
      })
      .catch((error) => {
        reject(error)
      })
  });
}

