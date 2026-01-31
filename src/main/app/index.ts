import './assets/style.css'

import { ApplicationCoordinatorFactory } from './coordinators/factory.ts'
import axios from 'axios'

import topbar from 'topbar'

topbar.config(window.$advancedChatConfiguration.loadingBar)

window.addEventListener('beforeunload', () => topbar.show())

const factory = new ApplicationCoordinatorFactory()

const session = await axios.get('/ui')

await factory.create(session.data)
