const showIcons = (event) => {
    const icons = event.target.querySelectorAll('.notes__list__table__note__action')
    icons.forEach(icon => icon.classList.remove("hidden"))
}
const hideIcons = (event) => {
    const icons = event.target.querySelectorAll('.notes__list__table__note__action')
    icons.forEach(icon => icon.classList.add("hidden"))
}

let notes = document.querySelectorAll('.notes__list__table__note')
notes.forEach(note => {
    note.addEventListener('mouseenter', showIcons)
    note.addEventListener('mouseleave', hideIcons)
})
