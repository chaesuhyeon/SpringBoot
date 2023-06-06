// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if(deleteButton) {
    deleteButton.addEventListener('click' , event => {
        let id = document.getElementById('article-id').value;

        fetch(`/api/articles/${id}` , {
            method : 'DELETE'
        })
        .then(() => {
            alert("삭제가 완료되었습니다.");
            location.replace('/articles'); /* location.replace : 실행 시 사용자의 웹 브라우저 화면을 현재 주소를 기반해 옮겨주는 역할을 함 */
        })
    })
}

// 수정
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`);
            });
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        fetch('/api/articles', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('등록 완료되었습니다.');
                location.replace('/articles');
            });
    });
}
