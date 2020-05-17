<template>
	<div id="app">
		<h1>
			<img src="./assets/logo.svg" alt="Enroller" class="logo">
			System do zapisów na zajęcia
		</h1>
		<div v-if="authenticatedUsername">
			<h2>Witaj {{ authenticatedUsername }}!
				<a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
			</h2>
			<meetings-page :username="authenticatedUsername"></meetings-page>
		</div>
		<div v-else>
			<button @click="openLoginForm()"
					:class="displayRegistrationForm ? 'button-outline' : ''">Loguję się</button>
			<button @click="openRegistrationForm()"
					:class="!displayRegistrationForm ? 'button-outline' : ''">Rejestruję się</button>

			<div v-if="error" class="error-alert">{{error}}</div>

			<login-form v-if="!displayRegistrationForm" @login="login($event)"></login-form>
			<login-form v-else @login="register($event)"
						button-label="Zarejestruj się"></login-form>
		</div>
	</div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";

    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
                authenticatedUsername: "",
                displayRegistrationForm: false,
				error: ""
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername = user.login;
            },
            logout() {
                this.authenticatedUsername = '';
            },
            register(user) {
                this.error = '';
                this.$http.post('participants', user)
                    .then(response => {
                        // udało się
                    })
                    .catch(response => {
                        this.error = "Taki użytkownik już istnieje!"
                    });
            },
            openLoginForm() {
                this.displayRegistrationForm = false;
            },
            openRegistrationForm() {
                this.displayRegistrationForm = true;
            }
        }
    };
</script>

<style>
	#app {
		max-width: 1000px;
		margin: 0 auto;
	}

	.logo {
		vertical-align: middle;
	}

	.error-alert {
		border: 2px solid red;
		background: pink;
		padding: 10px;
		text-align: center;
	}
</style>

