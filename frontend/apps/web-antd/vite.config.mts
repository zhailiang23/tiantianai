import { defineConfig } from '@vben/vite-config';

export default defineConfig(async () => {
  return {
    application: {},
    vite: {
      server: {
        proxy: {
          '/api': {
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, ''),
            // 后端 API 地址
            target: 'http://localhost:8080',
            ws: true,
          },
        },
      },
    },
  };
});
