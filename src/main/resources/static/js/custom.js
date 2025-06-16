function updateFileList(files) {
    const fileList = document.getElementById('fileList');
    const fileListContainer = document.getElementById('fileListContainer');
    fileList.innerHTML = '';

    if (files.length === 0) {
        fileListContainer.style.display = 'none';
        return;
    }

    for (let i = 0; i < files.length; i++) {
        const li = document.createElement('li');
        li.textContent = files[i].name;
        fileList.appendChild(li);
    }

    fileListContainer.style.display = 'block';
}

document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('file');

    const serverFiles = document.querySelectorAll('#fileList li');
    const fileListContainer = document.getElementById('fileListContainer');

    if (serverFiles.length > 0) {
        fileListContainer.style.display = 'block';
    } else if (fileInput.files.length > 0) {
        updateFileList(fileInput.files);
    }

    document.querySelector('form[action="/upload"] button[type="submit"]')
        .addEventListener('click', function () {
            const files = fileInput.files;
            if (files.length > 0) {
                const fileNames = Array.from(files).map(file => file.name);
                localStorage.setItem('selectedFiles', JSON.stringify(fileNames));
            }
        });

    const savedFiles = localStorage.getItem('selectedFiles');
    if (savedFiles) {
        const fileNames = JSON.parse(savedFiles);
        const fileList = document.getElementById('fileList');

        fileNames.forEach(fileName => {
            const li = document.createElement('li');
            li.textContent = fileName;
            fileList.appendChild(li);
        });

        fileListContainer.style.display = 'block';
        localStorage.removeItem('selectedFiles');
    }
});