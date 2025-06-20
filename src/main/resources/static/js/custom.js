const KEY_FILES = 'doccraft.selectedFiles';
const KEY_COUNT = 'doccraft.selectedAuthorCount';

function updateFileList(files) {
    const fileList = document.getElementById('fileList');
    const fileListContainer = document.getElementById('fileListContainer');
    fileList.innerHTML = '';

    if (!files || files.length === 0) {
        fileListContainer.style.display = 'none';
        return;
    }

    Array.from(files).forEach(f => {
        const li = document.createElement('li');
        li.textContent = f;
        fileList.appendChild(li);
    });
    fileListContainer.style.display = 'block';
}

document.addEventListener('DOMContentLoaded', () => {
    const fileInput = document.getElementById('file');
    const authorSelect = document.getElementById('authorCount');
    const uploadForm = document.querySelector('form[action="/upload"]');

    const savedCount = localStorage.getItem(KEY_COUNT);
    if (savedCount) {
        authorSelect.value = savedCount;
    }

    const savedFiles = JSON.parse(localStorage.getItem(KEY_FILES) || '[]');
    if (savedFiles.length) {
        updateFileList(savedFiles);
    }

    fileInput.addEventListener('change', () => {
        const names = Array.from(fileInput.files).map(f => f.name);
        localStorage.setItem(KEY_FILES, JSON.stringify(names));
        updateFileList(names);
    });

    authorSelect.addEventListener('change', () => {
        localStorage.setItem(KEY_COUNT, authorSelect.value);
    });

    uploadForm.addEventListener('submit', () => {
        // если файлы в инпуте — обновить
        const names = Array.from(fileInput.files).map(f => f.name);
        localStorage.setItem(KEY_FILES, JSON.stringify(names));
        localStorage.setItem(KEY_COUNT, authorSelect.value);
    });
});