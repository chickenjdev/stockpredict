export interface Extras {}

export interface Extras2 {}

export interface Request {
  reference1: string;
  reference2: string;
  amount: number;
  type: number;
  extras: Extras2;
}

export interface Extras3 {
  10000: string;
  20000: string;
  30000: string;
  50000: string;
  100000: string;
  200000: string;
  300000: string;
  500000: string;
  1000000: string;
}

export interface BillList {
  extras: Extras3;
  reference2: string;
}

export interface TopupOld {
  smsContent: any[];
  extras: Extras;
  request: Request;
  billList: BillList[];
  resultCode: number;
  resultMessage: string;
}
