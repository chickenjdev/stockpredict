// @ts-nocheck
import React from 'react';
import { ApplyPluginsType, dynamic } from '/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/node_modules/@umijs/runtime';
import * as umiExports from './umiExports';
import { plugin } from './plugin';
import LoadingComponent from '@/components/PageLoading/index';

export function getRoutes() {
  const routes = [
  {
    "path": "/",
    "component": dynamic({ loader: () => import(/* webpackChunkName: 'layouts__BlankLayout' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/layouts/BlankLayout'), loading: LoadingComponent}),
    "routes": [
      {
        "path": "/user",
        "component": dynamic({ loader: () => import(/* webpackChunkName: 'layouts__UserLayout' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/layouts/UserLayout'), loading: LoadingComponent}),
        "routes": [
          {
            "path": "/user/login",
            "name": "login",
            "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__User__login' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/User/login'), loading: LoadingComponent}),
            "exact": true
          },
          {
            "path": "/user",
            "redirect": "/user/login",
            "exact": true
          },
          {
            "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__404' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/404'), loading: LoadingComponent}),
            "exact": true
          }
        ]
      },
      {
        "path": "/",
        "component": dynamic({ loader: () => import(/* webpackChunkName: 'layouts__SecurityLayout' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/layouts/SecurityLayout'), loading: LoadingComponent}),
        "Routes": [
          "src/pages/Authorized"
        ],
        "authority": [
          "admin",
          "user"
        ],
        "routes": [
          {
            "path": "/",
            "component": dynamic({ loader: () => import(/* webpackChunkName: 'layouts__BasicLayout' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/layouts/BasicLayout'), loading: LoadingComponent}),
            "authority": [
              "admin",
              "user"
            ],
            "routes": [
              {
                "path": "/",
                "redirect": "/dashboard/buycard",
                "exact": true
              },
              {
                "path": "/welcome",
                "icon": "smile",
                "name": "Welcome",
                "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__welcome' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/welcome'), loading: LoadingComponent}),
                "exact": true
              },
              {
                "path": "/dashboard",
                "name": "dashboard",
                "icon": "dashboard",
                "routes": [
                  {
                    "path": "/",
                    "redirect": "/dashboard/buycard",
                    "exact": true
                  },
                  {
                    "name": "Realtime",
                    "icon": "smile",
                    "path": "/dashboard/buycard",
                    "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__dashboard__buycard' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/dashboard/buycard'), loading: LoadingComponent}),
                    "exact": true
                  },
                  {
                    "name": "Predict",
                    "icon": "smile",
                    "path": "/dashboard/buycarddata",
                    "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__dashboard__buycarddata' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/dashboard/buycarddata'), loading: LoadingComponent}),
                    "exact": true
                  },
                  {
                    "name": "History",
                    "icon": "smile",
                    "path": "/dashboard/topup",
                    "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__dashboard__topup' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/dashboard/topup'), loading: LoadingComponent}),
                    "exact": true
                  }
                ]
              },
              {
                "path": "/history",
                "icon": "history",
                "name": "History",
                "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__history' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/history'), loading: LoadingComponent}),
                "exact": true
              },
              {
                "name": "account",
                "icon": "user",
                "path": "/account",
                "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__account' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/account'), loading: LoadingComponent}),
                "exact": true
              },
              {
                "component": dynamic({ loader: () => import(/* webpackChunkName: 'p__404' */'/home/khoinguyen/khoinguyen/KhoaLuan/stockpredict/StockPredictionFE/src/pages/404'), loading: LoadingComponent}),
                "exact": true
              }
            ]
          }
        ]
      }
    ]
  }
];

  // allow user to extend routes
  plugin.applyPlugins({
    key: 'patchRoutes',
    type: ApplyPluginsType.event,
    args: { routes },
  });

  return routes;
}
