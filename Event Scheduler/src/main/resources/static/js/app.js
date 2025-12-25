/*
 Minimal event-scheduler client logic.
 Place this file at: src/main/resources/static/js/app.js
*/
$(function () {
    const timeRe = /^([01]\d|2[0-3]):([0-5]\d)$/;
    let workingStart = null;
    let workingEnd = null;
    const events = [];

    function parseMinutes(t) {
        const m = timeRe.exec(t);
        if (!m) return null;
        return parseInt(m[1], 10) * 60 + parseInt(m[2], 10);
    }

    function formatTimeMinutes(mins) {
        const h = String(Math.floor(mins / 60)).padStart(2, '0');
        const m = String(mins % 60).padStart(2, '0');
        return `${h}:${m}`;
    }

    function renderEvents() {
        const $list = $('#eventList').empty();
        if (events.length === 0) {
            $list.append('<li>No events scheduled</li>');
            return;
        }
        events
            .slice()
            .sort((a, b) => a.start - b.start)
            .forEach(ev => {
                $list.append(`<li>${ev.name} — ${formatTimeMinutes(ev.start)} to ${formatTimeMinutes(ev.end)}</li>`);
            });
    }

    function findConflicts(start, end) {
        return events.filter(ev => !(end <= ev.start || start >= ev.end));
    }

    $('#setHours').on('click', function () {
        const v = $('#workingHours').val().trim();
        const parts = v.split('-').map(s => s.trim());
        if (parts.length !== 2) {
            alert('Please use format HH:MM - HH:MM');
            return;
        }
        const s = parseMinutes(parts[0]);
        const e = parseMinutes(parts[1]);
        if (s === null || e === null) {
            alert('Invalid time format. Use HH:MM');
            return;
        }
        if (s >= e) {
            alert('Start must be before end.');
            return;
        }
        workingStart = s;
        workingEnd = e;
        alert(`Working hours set: ${formatTimeMinutes(workingStart)} - ${formatTimeMinutes(workingEnd)}`);
    });

    $('#addEvent').on('click', function () {
        const name = $('#eventName').val().trim() || 'Unnamed';
        const sText = $('#startTime').val().trim();
        const eText = $('#endTime').val().trim();

        const s = parseMinutes(sText);
        const e = parseMinutes(eText);
        if (s === null || e === null) {
            alert('Start or end time invalid. Use HH:MM');
            return;
        }
        if (s >= e) {
            alert('Event start must be before end.');
            return;
        }
        if (workingStart !== null && (s < workingStart || e > workingEnd)) {
            alert('Event is outside working hours.');
            return;
        }

        const conflicts = findConflicts(s, e);
        if (conflicts.length > 0) {
            $('#conflictMessage').text(`Event "${name}" conflicts with ${conflicts.length} existing event(s). Overwrite them?`);
            $('#conflictResolution').show();
            // store pending event on the button for resolution
            $('#resolveConflict').data('pending', { name, start: s, end: e, conflicts: conflicts.map(c => c.id) });
            return;
        }

        // add event
        events.push({ id: Date.now() + Math.random(), name, start: s, end: e });
        renderEvents();
        $('#eventName').val(''); $('#startTime').val(''); $('#endTime').val('');
    });

    $('#resolveConflict').on('click', function () {
        const pending = $(this).data('pending');
        if (!pending) return;
        // remove conflicting events
        for (let i = events.length - 1; i >= 0; i--) {
            if (pending.conflicts.includes(events[i].id)) events.splice(i, 1);
        }
        events.push({ id: Date.now() + Math.random(), name: pending.name, start: pending.start, end: pending.end });
        $('#conflictResolution').hide();
        $(this).removeData('pending');
        renderEvents();
    });

    // initial render
    renderEvents();

    // helpful debug hint: open DevTools and check console for errors
    console.log('app.js loaded');
});