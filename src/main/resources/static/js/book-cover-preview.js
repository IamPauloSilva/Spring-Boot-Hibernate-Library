(function () {
    var fileInput = document.querySelector('[data-cover-input]');
    var preview = document.querySelector('[data-cover-preview]');

    if (!fileInput || !preview) {
        return;
    }

    var defaultSrc = '/img/cover-placeholder.svg';

    fileInput.addEventListener('change', function (event) {
        var file = event.target.files && event.target.files[0];

        if (!file) {
            preview.src = preview.getAttribute('data-initial-src') || defaultSrc;
            return;
        }

        if (!file.type || file.type.indexOf('image/') !== 0) {
            fileInput.value = '';
            preview.src = preview.getAttribute('data-initial-src') || defaultSrc;
            return;
        }

        var objectUrl = URL.createObjectURL(file);
        preview.src = objectUrl;
    });

    if (!preview.getAttribute('data-initial-src')) {
        preview.setAttribute('data-initial-src', preview.getAttribute('src') || defaultSrc);
    }
})();

