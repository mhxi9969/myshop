import userApi from '~/api/user/user'

export default async function ({ redirect }) {
    try {
        const res = await userApi.getUserBySession()
        const role = res.data.data.record.role

        if (!role || (role !== 'ADMIN' && role !== 'USER')) {
            return redirect('/login')
        }
    } catch (err) {
        return redirect('/login')
    }
}
