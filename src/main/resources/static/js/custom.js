function updateFileList(files) {
    const fileList = document.getElementById('fileList');
    const fileListContainer = document.getElementById('fileListContainer');

    if (!fileList || !fileListContainer) {
        console.error("File list elements not found!");
        return;
    }

    fileList.innerHTML = '';

    if (!files || files.length === 0) {
        fileListContainer.style.display = 'none';
        return;
    }

    files.forEach(f => {
        const li = document.createElement('li');
        li.textContent = f;
        fileList.appendChild(li);
    });

    fileListContainer.style.display = 'block';
}

document.addEventListener('DOMContentLoaded', () => {
    const fileInput = document.getElementById('file');
    const packageSelect = document.getElementById('packageId');

 function initializeFileList() {
        try {
            if (serverFileNames && serverFileNames.length > 0) {
                updateFileList(serverFileNames);
            } else {
                const fileListContainer = document.getElementById('fileListContainer');
                if (fileListContainer) {
                    fileListContainer.style.display = 'none';
                }
            }
        } catch (e) {
            console.error("Error initializing file list:", e);
        }
    }

    function refreshFileList() {
        const names = Array.from(fileInput.files).map(f => f.name);
        const packageId = packageSelect ? packageSelect.value : null;

        if (packageId) {
            fetch(`/packages/${packageId}/files`)
                .then(res => {
                    if (!res.ok) {
                        throw new Error(`HTTP error! status: ${res.status}`);
                    }
                    return res.json();
                })
                .then(packageFiles => {
                    const allNames = [...names, ...packageFiles];
                    updateFileList(allNames);
                })
                .catch(error => {
                    updateFileList(names);
                });
        } else {
            updateFileList(names);
        }
    }

    if (fileInput) {
        fileInput.addEventListener('change', refreshFileList);
    } else {
        console.error("File input element not found");
    }

    if (packageSelect) {
        packageSelect.addEventListener('change', refreshFileList);
    } else {
        console.log("Package select element not found - packages might be empty");
    }

    initializeFileList();
});