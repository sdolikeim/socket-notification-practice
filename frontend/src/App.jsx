import { useEffect, useState } from 'react'
import axios from 'axios'
import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import './App.css'

function App() {
  const [memberName, setMemberName] = useState('홍길동')
  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')
  const [articles, setArticles] = useState([])
  const [answer, setAnswer] = useState('')
  const [selectedArticleId, setSelectedArticleId] = useState(null)
  const [adminNotice, setAdminNotice] = useState('')
  const [userNotice, setUserNotice] = useState('')

  const API_URL = 'http://localhost:8087/api/articles'

  const fetchArticles = async () => {
    const response = await axios.get(API_URL)
    setArticles(response.data)
  }

  const createArticle = async () => {
    if (!title.trim() || !content.trim()) {
      alert('제목과 내용을 입력해주세요.')
      return
    }

    await axios.post(API_URL, {
      memberName,
      title,
      content,
    })

    setTitle('')
    setContent('')

    await fetchArticles()
  }

  const submitAnswer = async () => {
    if (!selectedArticleId) {
      alert('답변할 상담을 선택해주세요.')
      return
    }

    if (!answer.trim()) {
      alert('답변 내용을 입력해주세요.')
      return
    }

    await axios.put(`${API_URL}/answer`, {
      articleId: selectedArticleId,
      answer,
    })

    setSelectedArticleId(null)
    setAnswer('')

    await fetchArticles()
  }

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8087/ws'),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('WebSocket connected')

        client.subscribe('/topic/admin', (message) => {
          const article = JSON.parse(message.body)
          setAdminNotice(`새 상담 문의가 등록되었습니다: ${article.title}`)
        })

        client.subscribe('/topic/user', (message) => {
          const article = JSON.parse(message.body)
          setUserNotice(`답변이 등록되었습니다: ${article.title}`)
        })
      },
    })

    client.activate()

    return () => {
      client.deactivate()
    }
  }, [])

  return (
    <div className="app">
      <header>
        <p>Spring Boot WebSocket Practice</p>
        <h1>실시간 상담 알림 실습</h1>
        <span>
          Oracle DB에 상담 문의를 저장하고, WebSocket으로 관리자와 사용자에게 알림을 보내는 실습 프로젝트입니다.
        </span>
      </header>

      <main>
        <section className="panel">
          <h2>사용자 상담 문의 등록</h2>

          {userNotice && <div className="notice user-notice">{userNotice}</div>}

          <label>
            사용자 이름
            <input
              value={memberName}
              onChange={(e) => setMemberName(e.target.value)}
              placeholder="사용자 이름"
            />
          </label>

          <label>
            문의 제목
            <input
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="문의 제목"
            />
          </label>

          <label>
            문의 내용
            <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="문의 내용을 입력하세요."
            />
          </label>

          <button onClick={createArticle}>문의 등록</button>
        </section>

        <section className="panel">
          <h2>관리자 상담 관리</h2>

          {adminNotice && <div className="notice admin-notice">{adminNotice}</div>}

          <button className="refresh-button" onClick={fetchArticles}>
            상담 목록 새로고침
          </button>

          <div className="article-list">
            {articles.length === 0 ? (
              <p className="empty">등록된 상담 문의가 없습니다.</p>
            ) : (
              articles.map((article) => (
                <div
                  className={
                    selectedArticleId === article.articleId
                      ? 'article-card selected'
                      : 'article-card'
                  }
                  key={article.articleId}
                  onClick={() => setSelectedArticleId(article.articleId)}
                >
                  <div className="article-top">
                    <strong>{article.title}</strong>
                    <span className={article.status === 'ANSWERED' ? 'answered' : 'waiting'}>
                      {article.status}
                    </span>
                  </div>

                  <p>{article.content}</p>
                  <small>작성자: {article.memberName}</small>

                  {article.answer && (
                    <div className="answer-box">
                      <strong>관리자 답변</strong>
                      <p>{article.answer}</p>
                    </div>
                  )}
                </div>
              ))
            )}
          </div>

          <div className="answer-form">
            <h3>관리자 답변 등록</h3>
            <textarea
              value={answer}
              onChange={(e) => setAnswer(e.target.value)}
              placeholder="답변 내용을 입력하세요."
            />
            <button onClick={submitAnswer}>답변 등록</button>
          </div>
        </section>
      </main>
    </div>
  )
}

export default App