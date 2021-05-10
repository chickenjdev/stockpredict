import { API_TELCO } from '@/utils/constants';
import MServiceRequest, { environment } from '@/utils/MServiceRequest';

export async function getBalancing(params: any) {
  return MServiceRequest(API_TELCO.GET_BALANCING, {
    method: 'GET',
    params,
    mode: 'no-cors',
    headers: {
      Authorization:
        'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjoiMDkxNDExMTExMSIsIk5BTUUiOiJBbmggRHVjIDEiLCJpbWVpIjoiNmU5MzYxZDgtNzY1My00ZDM0LWI4ZTQtMjQzNTlkZjA3NzIxIiwiYWdlbnRfaWQiOjM3MDA4NDI3LCJCQU5LX0NPREUiOiIzMDEiLCJCQU5LX05BTUUiOiJWaWV0Y29tYmFuayIsIlZBTElEX1dBTExFVF9DT05GSVJNIjoxLCJNQVBfU0FDT01fQ0FSRCI6MCwiSURFTlRJRlkiOiJDT05GSVJNIiwiREVWSUNFX09TIjoiQU5EUk9JRCIsInNlc3Npb25LZXkiOiJET0lzRFo4L2ZQeDJuc001NzRtRXE0TGxXL0J5SUhSWWt1Q1Z0NFFYdTdaajJaV3kxNHdBdWc9PSIsInBpbiI6Ii9wNVR3a3FnalFZPSIsInBpbl9lbmNyeXB0Ijp0cnVlLCJ2ZXJpZnlJbmZvIjoiMTIzNCIsImJhbmtWZXJpZnlOYW1lIjoiUEhBVCBEQVQiLCJiYW5rVmVyaWZ5UGVyc29uYWxJZCI6IjEyMzQ1NiIsImxldmVsIjowLCJBUFBfVkVSIjozMDAwMiwiaWF0IjoxNjEwODgxMzU0fQ.Jm-rVFZIFCacoXQ9kppwNAAHGe8DZNT3BDMeSLDqem5I881YN1iM9nRQC-avnayy2xQZTln1GzBnPBbAjl8Dqq1sDRub70jGAhy4HDKlTBLNNyznrztNrxdQnT4zhSGW9YIurGOvvYhTqi288P25SuP0Os2-h-NubtOkoo8uQxp7t1baSC2ybTElo9OybI-otNmCzr2RRbBg5rQf_FGheeI11E5l2A5gVwRXaq7rKZsYZN8KxVk_hSkB0Na4H9q7ZV_BOw8ufEgvLBSKdXcngTfPIutPKt2rwLopECPns4fUDm-MaDqrz7Q2JHy3VqeTHRpPLz7x-5_KwPKlWdq-Ww',
    },
    prefix: environment(),
  });
}
