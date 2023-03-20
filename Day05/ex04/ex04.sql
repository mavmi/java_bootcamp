with userToOwnRooms as (
    select
        "user".id as user_id,
        "user".login as user_login,
        "user".password as user_passwd,
        chatroom.id as own_room_id,
        chatroom.name as own_room_name
    from "user"
        full join chatroom
            on "user".id = chatroom.owner
), userToAllRooms as (
    select
        userToOwnRooms.user_id,
        userToOwnRooms.user_login,
        userToOwnRooms.user_passwd,
        userToOwnRooms.own_room_id,
        chatroom.id as part_of_room_id
    from userToOwnRooms
        full join message
            on userToOwnRooms.user_id = message.author
        full join chatroom
            on message.room = chatroom.id
)

select * from userToAllRooms
where user_id is not null
order by user_id, own_room_id, part_of_room_id;
