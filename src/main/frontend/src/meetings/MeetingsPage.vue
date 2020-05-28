<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: []
            };
        },

        methods: {
            addNewMeeting(meeting) {
                this.$http.post('meetings', meeting)
                    .then(response => {
                        this.meetings.push(response.body);
                    })
                    .catch(response => alert('Błąd przy dodawaniu spotkania. Kod odpowiedzi: ' + response.status));
            },
            addMeetingParticipant(meeting) {
                let participant = {};
                participant.login = this.username;

                this.$http.post('meetings/' + meeting.id + '/participants', participant)
                        .then(response => meeting.participants.push(participant))
                        .catch(response => alert('Błąd przy zapisywaniu na spotkanie. Kod odpowiedzi: ' + response.status));
            },
            removeMeetingParticipant(meeting) {
                this.$http.delete('meetings/' + meeting.id + '/participants/' + this.username)
                        .then(response =>meeting.participants.splice(meeting.participants.indexOf(this.username), 1))
                        .catch(response => alert('Błąd przy usuwaniu ze spotkania. Kod odpowiedzi: ' + response.status));
            },
            deleteMeeting(meeting) {
                this.$http.delete('meetings/' + meeting.id)
                    .then(response => {
                        this.meetings.splice(this.meetings.indexOf(meeting), 1);
                    })
                    .catch(response => alert('Błąd przy usuwaniu spotkania. Kod odpowiedzi: ' + response.status));
            }
        },

        mounted() {
            this.$http.get('meetings')
            .then(response => {
                this.meetings.push(...response.body);
            })
            .catch(response => alert('Błąd przy pobieraniu listy spotkań. Kod odpowiedzi: ' + response.status));
        }
    }
</script>
