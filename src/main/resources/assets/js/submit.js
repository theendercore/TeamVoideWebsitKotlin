// htmx.logAll()
document.body.addEventListener('htmx:configRequest', evt => {
    if (evt.target.id === 'download') evt.detail.parameters['data-id'] = download()
})

function download() {
    return Array
        .from(document.getElementById('pack-list').children)
        .map(it => it.getAttribute('data-id'))
        .join(',')
}

/**
 * Fun that closes the modal
 * @param {Event} event Event
 */
function closeModal(event) {
    const modal = document.getElementById('modalId')
    if (event.target === modal) modal.remove()
}
