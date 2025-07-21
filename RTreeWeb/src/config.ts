
import { Configuration, RTreeDoubleApi } from '@/generated/TypeScriptClient';

export const api = new RTreeDoubleApi(
  new Configuration({
    // read from environment variable or set default
    // basePath: process.env.VUE_APP_RTREE_API_BASE_PATH || 'http://localhost:8080',
    basePath: 'http://localhost:8080',
  })
);