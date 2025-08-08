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
    const packageSelect = document.getElementById('packageId');
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
        refreshFileList();
    });

    packageSelect.addEventListener('change', () => {
        refreshFileList();
    });

    authorSelect.addEventListener('change', () => {
        localStorage.setItem(KEY_COUNT, authorSelect.value);
    });

    uploadForm.addEventListener('submit', () => {
        refreshFileList();
        localStorage.setItem(KEY_COUNT, authorSelect.value);
    });

    function refreshFileList() {
        const names = Array.from(fileInput.files).map(f => f.name);
        const packageId = packageSelect.value;
        if (packageId) {
            fetch(`/packages/${packageId}/files`)
                .then(res => res.json())
                .then(packageFiles => {
                    const allNames = [...names, ...packageFiles];
                    localStorage.setItem(KEY_FILES, JSON.stringify(allNames));
                    updateFileList(allNames);
                });
        } else {
            localStorage.setItem(KEY_FILES, JSON.stringify(names));
            updateFileList(names);
        }
    }
});