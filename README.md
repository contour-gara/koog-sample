# koog-sample

## フロントエンド (React + TypeScript)

`frontend` ディレクトリに Vite ベースの SPA を追加しました。日本語テキストを入力し、`http://localhost:8080/translate` の翻訳 API を呼び出して英語訳を表示します。

### 起動手順

1. `cd frontend`
2. `npm install`
3. `npm run dev`

Vite の開発サーバーはデフォルトで `http://localhost:5173` で起動します。バックエンドの API は `http://localhost:8080/translate` を想定しており、開発時は Vite のプロキシで `/translate` へのリクエストがバックエンドに転送されます。エンドポイントのパスが異なる場合は `frontend/src/App.tsx` の `API_ENDPOINT` 定数と `frontend/vite.config.ts` のプロキシ設定を調整してください。
