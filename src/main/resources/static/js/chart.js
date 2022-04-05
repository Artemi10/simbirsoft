const showSelect = () => {
    const elems = document.querySelectorAll('select');
    const instances = M.FormSelect.init(elems, {});
}

const showDatePickers = () => {
    const datePickers = document.querySelectorAll('.datepicker');
    const dateOptions = {
        firstDay : 1,
        format : 'dd.mm.yyyy',
        i18n : {
            months : ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь',
                'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
            monthsShort : ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл',
                'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'],
            weekdays : ['Воскресенье', 'Понедельник', 'Вторник', 'Среда',
                'Четверг', 'Пятница', 'Суббота'],
            weekdaysShort : ['Вск', 'Пнд', 'Втр', 'Сре', 'Чтв', 'Птн', 'Суб'],
            weekdaysAbbrev : ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'],
            cancel : 'Закрыть',
            clear : 'Очистить',
            done : 'Выбрать'
        }
    }
    datePickers.forEach(datePicker => M.Datepicker.init(datePicker, dateOptions));
}

const openSettings = () => {
    const statsPanel = document.querySelector('.stats__form__container');
    statsPanel.style.display = 'flex';
}

const closeSettings = () => {
    const statsPanel = document.querySelector('.stats__form__container');
    statsPanel.style.display = 'none';
}

document.addEventListener('DOMContentLoaded', showSelect);

document.addEventListener('DOMContentLoaded', showDatePickers);

document.querySelector('.setting-icon')
    .addEventListener('click', openSettings);

document.querySelector('.stats__form__icon_close')
    .addEventListener('click', closeSettings);
