import { FormEvent, useState } from "react";
import "./App.css";

type TranslateResponse = {
  translatedText: string;
};

const API_ENDPOINT = "/translate";

function App() {
  const [sourceText, setSourceText] = useState("");
  const [translatedText, setTranslatedText] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    if (!sourceText.trim() || loading) {
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await fetch(API_ENDPOINT, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ text: sourceText.trim() })
      });

      if (!response.ok) {
        throw new Error(`APIからエラー応答 (${response.status}) が返されました。`);
      }

      const data = (await response.json()) as TranslateResponse;
      setTranslatedText(data.translatedText ?? "");
    } catch (err) {
      setError(err instanceof Error ? err.message : "翻訳に失敗しました。");
      setTranslatedText("");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <h1 className="title">日→英 翻訳デモ</h1>
      <form className="translator" onSubmit={handleSubmit}>
        <label className="field">
          <span className="field-label">日本語</span>
          <textarea
            className="textarea"
            value={sourceText}
            placeholder="ここに日本語を入力してください"
            onChange={(event) => setSourceText(event.target.value)}
            rows={10}
          />
        </label>
        <div className="actions">
          <button
            className="translate-button"
            type="submit"
            disabled={loading || !sourceText.trim()}
          >
            {loading ? "翻訳中…" : "翻訳"}
          </button>
        </div>
        <label className="field">
          <span className="field-label">英語</span>
          <textarea
            className="textarea"
            value={translatedText}
            placeholder="翻訳結果がここに表示されます"
            readOnly
            rows={10}
          />
        </label>
      </form>
      {error && <p className="error">{error}</p>}
    </div>
  );
}

export default App;
