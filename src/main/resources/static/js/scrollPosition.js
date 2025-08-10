document.addEventListener('DOMContentLoaded', function () {
    const deleteForms = document.querySelectorAll('.delete-file-form');

    deleteForms.forEach(form => {
        form.addEventListener('submit', function (event) {
            event.preventDefault();

            if (!confirm('Удалить этот файл?')) {
                return;
            }

            const formAction = form.getAttribute('action');
            const csrfToken = form.querySelector('input[name]').value;
            const fileId = formAction.split('/').pop();
            const listItem = form.closest('.list-group-item');

            fetch(formAction, {
                method: 'POST',
                headers: {
                    'X-CSRF-TOKEN': csrfToken,
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    '_csrf': csrfToken
                })
            })
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw new Error('Network response was not ok.');
                }
            })
            .then(html => {
                if (listItem) {
                    listItem.remove();
                }
            })
            .catch(error => {
                console.error('Ошибка при удалении файла:', error);
                alert('Ошибка удаления файла. Пожалуйста, попробуйте еще раз.');
            });
        });
    });
});