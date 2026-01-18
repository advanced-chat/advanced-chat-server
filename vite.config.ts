import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from '@tailwindcss/vite'
import vueDevTools from 'vite-plugin-vue-devtools'
import * as path from "node:path";

import viteSpringBootPlugin from './src/main/vite/spring-boot-plugin'

export default defineConfig({
    plugins: [
        vue(),
        tailwindcss(),
        vueDevTools(),
        viteSpringBootPlugin({
            verbose: true
        })
    ],
    root: path.join(__dirname, './src/main/vue'),
    build: {
        manifest: true,
        rollupOptions: {
            input: [
                './src/main/vue/main.ts'
            ]
        },
        outDir: path.join(__dirname, `./build/resources/main/static`),
        emptyOutDir: true
    }
})
